// DatabaseModule.kt
package com.offthephone.database

import android.content.Context
import androidx.room.*
import androidx.room.RoomDatabase
import java.util.*
import kotlinx.coroutines.flow.Flow

// Entity Classes
@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val description: String,
    @ColumnInfo(name = "created_date")
    val createdDate: Date,
    @ColumnInfo(name = "due_date")
    val dueDate: Date,
    @ColumnInfo(name = "is_completed")
    val isCompleted: Boolean = false,
    val duration: Int, // in minutes
    val priority: TaskPriority,
    @ColumnInfo(name = "reminder_time")
    val reminderTime: Date?
)

@Entity(tableName = "screen_time_limits")
data class ScreenTimeLimit(
    @PrimaryKey
    @ColumnInfo(name = "package_name")
    val packageName: String,
    @ColumnInfo(name = "app_name")
    val appName: String,
    @ColumnInfo(name = "daily_limit")
    val dailyLimit: Int, // in minutes
    @ColumnInfo(name = "is_enabled")
    val isEnabled: Boolean = true
)

@Entity(tableName = "screen_time_logs")
data class ScreenTimeLog(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "package_name")
    val packageName: String,
    @ColumnInfo(name = "usage_date")
    val usageDate: Date,
    @ColumnInfo(name = "usage_time")
    val usageTime: Int // in minutes
)

enum class TaskPriority {
    LOW, MEDIUM, HIGH
}

// Type Converters
class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun fromPriority(priority: TaskPriority): String {
        return priority.name
    }

    @TypeConverter
    fun toPriority(priority: String): TaskPriority {
        return TaskPriority.valueOf(priority)
    }
}

// Data Access Objects (DAOs)
@Dao
interface TaskDao {
    @Query("SELECT * FROM tasks ORDER BY due_date ASC")
    fun getAllTasksFlow(): Flow<List<Task>>

    @Query("SELECT * FROM tasks WHERE is_completed = 0 ORDER BY due_date ASC")
    fun getPendingTasksFlow(): Flow<List<Task>>

    @Query("SELECT * FROM tasks WHERE id = :taskId")
    suspend fun getTaskById(taskId: Int): Task?

    @Query("SELECT * FROM tasks WHERE due_date BETWEEN :startDate AND :endDate")
    suspend fun getTasksForDateRange(startDate: Date, endDate: Date): List<Task>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: Task): Long

    @Update
    suspend fun updateTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)

    @Query("DELETE FROM tasks WHERE is_completed = 1")
    suspend fun deleteCompletedTasks()
}

@Dao
interface ScreenTimeLimitDao {
    @Query("SELECT * FROM screen_time_limits")
    fun getAllLimitsFlow(): Flow<List<ScreenTimeLimit>>

    @Query("SELECT * FROM screen_time_limits WHERE package_name = :packageName")
    suspend fun getLimitForPackage(packageName: String): ScreenTimeLimit?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLimit(limit: ScreenTimeLimit)

    @Delete
    suspend fun deleteLimit(limit: ScreenTimeLimit)

    @Query("UPDATE screen_time_limits SET is_enabled = :isEnabled WHERE package_name = :packageName")
    suspend fun updateLimitStatus(packageName: String, isEnabled: Boolean)
}

@Dao
interface ScreenTimeLogDao {
    @Query("SELECT * FROM screen_time_logs WHERE usage_date BETWEEN :startDate AND :endDate")
    suspend fun getLogsForDateRange(startDate: Date, endDate: Date): List<ScreenTimeLog>

    @Query("""
        SELECT SUM(usage_time) 
        FROM screen_time_logs 
        WHERE package_name = :packageName 
        AND usage_date = :date
    """)
    suspend fun getTotalUsageForDay(packageName: String, date: Date): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLog(log: ScreenTimeLog)

    @Query("DELETE FROM screen_time_logs WHERE usage_date < :date")
    suspend fun deleteOldLogs(date: Date)
}

// Database Class
@Database(
    entities = [Task::class, ScreenTimeLimit::class, ScreenTimeLog::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
    abstract fun screenTimeLimitDao(): ScreenTimeLimitDao
    abstract fun screenTimeLogDao(): ScreenTimeLogDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "off_the_phone_db"
                )
                    .addCallback(object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            // Add any initial data if needed
                        }
                    })
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

// Repository class for handling database operations
class AppRepository(private val database: AppDatabase) {
    // Task Operations
    fun getAllTasks() = database.taskDao().getAllTasksFlow()
    fun getPendingTasks() = database.taskDao().getPendingTasksFlow()

    suspend fun addTask(task: Task) = database.taskDao().insertTask(task)
    suspend fun updateTask(task: Task) = database.taskDao().updateTask(task)
    suspend fun deleteTask(task: Task) = database.taskDao().deleteTask(task)

    // Screen Time Limit Operations
    fun getAllLimits() = database.screenTimeLimitDao().getAllLimitsFlow()

    suspend fun setAppLimit(limit: ScreenTimeLimit) =
        database.screenTimeLimitDao().insertLimit(limit)

    suspend fun getAppLimit(packageName: String) =
        database.screenTimeLimitDao().getLimitForPackage(packageName)

    // Screen Time Log Operations
    suspend fun logScreenTime(log: ScreenTimeLog) =
        database.screenTimeLogDao().insertLog(log)

    suspend fun getDailyUsage(packageName: String, date: Date) =
        database.screenTimeLogDao().getTotalUsageForDay(packageName, date)

    suspend fun getUsageHistory(startDate: Date, endDate: Date) =
        database.screenTimeLogDao().getLogsForDateRange(startDate, endDate)

    // Cleanup Operations
    suspend fun cleanupOldData(date: Date) =
        database.screenTimeLogDao().deleteOldLogs(date)

    suspend fun removeCompletedTasks() =
        database.taskDao().deleteCompletedTasks()
}