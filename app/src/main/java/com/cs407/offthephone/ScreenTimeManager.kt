package com.cs407.offthephone

import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.provider.Settings
import android.util.Log

class ScreenTimeManager(private val context: Context) {

    /**
     * Fetches the top N most-used apps with their screen time.
     * @param topN Number of top apps to fetch (default: 3).
     * @return A list of pairs (App Name, Screen Time in milliseconds).
     */
    fun getTopUsedApps(topN: Int = 3): List<Pair<String, Long>> {
        if (!isUsageStatsPermissionGranted()) {
            Log.e("ScreenTimeManager", "Usage stats permission not granted.")
            return emptyList()
        }

        val usageStatsManager = context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager

        // Query usage stats for the last 24 hours
        val endTime = System.currentTimeMillis()
        val startTime = endTime - 24 * 60 * 60 * 1000 // 24 hours ago

        val usageStatsList: List<UsageStats> = usageStatsManager.queryUsageStats(
            UsageStatsManager.INTERVAL_DAILY, startTime, endTime
        )

        if (usageStatsList.isNullOrEmpty()) {
            Log.e("ScreenTimeManager", "No usage stats available.")
            return emptyList()
        }

        // Map package names to screen time and sort by usage time
        val appUsageMap = usageStatsList
            .filter { it.totalTimeInForeground > 0 } // Only include apps with usage
            .associateBy({ it.packageName }, { it.totalTimeInForeground })

        // Get top N apps and resolve package names to app names
        return appUsageMap.entries
            .sortedByDescending { it.value }
            .take(topN)
            .map { (packageName, screenTime) ->
                getAppNameFromPackage(packageName) to screenTime
            }
    }

    /**
     * Checks if usage stats permission is granted.
     * @return True if granted, false otherwise.
     */
    fun isUsageStatsPermissionGranted(): Boolean {
        val usageStatsManager = context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        val endTime = System.currentTimeMillis()
        val startTime = endTime - 1000 // Small time range to check
        val stats = usageStatsManager.queryUsageStats(
            UsageStatsManager.INTERVAL_DAILY, startTime, endTime
        )
        return stats.isNotEmpty()
    }

    /**
     * Redirects the user to grant usage stats permission.
     */
    fun requestUsageStatsPermission() {
        val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
        context.startActivity(intent)
    }

    /**
     * Resolves a package name to a user-friendly app name.
     * @param packageName The package name of the app.
     * @return The user-friendly app name, or the package name if the app name cannot be resolved.
     */
    private fun getAppNameFromPackage(packageName: String): String {
        return try {
            val packageManager: PackageManager = context.packageManager
            val appInfo: ApplicationInfo = packageManager.getApplicationInfo(packageName, 0)
            packageManager.getApplicationLabel(appInfo).toString()
        } catch (e: PackageManager.NameNotFoundException) {
            Log.e("ScreenTimeManager", "App name not found for package: $packageName")
            packageName
        }
    }

    /**
     * Sends the top N used apps' information via a callback.
     * @param topN Number of top apps to fetch (default: 3).
     * @param callback Function to send the data to.
     */
    fun sendTopUsedApps(topN: Int = 3, callback: (List<Pair<String, Long>>) -> Unit) {
        val topApps = getTopUsedApps(topN)
        callback(topApps)
    }
}

// Example Usage:
// val screenTimeManager = ScreenTimeManager(context)
// screenTimeManager.sendTopUsedApps(3) { topApps ->
//     topApps.forEach { (appName, screenTime) ->
//         Log.d("TopApps", "App: $appName, Time: ${screenTime / 1000} seconds")
//     }
// }
