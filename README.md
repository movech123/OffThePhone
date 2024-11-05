# OffThePhone
CS 407 Project
Permissions Required:
For getting the user's screen time statistics
    permission: Manifest.permission.PACKAGE_USAGE_STATS
    Class: UsageStatsManager
For getting access to the user's flashlight
    Either get access to the camera: android.permission.CAMERA
    Or to their flashlight directly (not sure about this one): android.permission.FLASHLIGHT
For allowing running in the background, even when the app is closed:
    used for notifications (and potentially a screen on alarm)
    Permission required: android.permission.FOREGROUND_SERVICE

Fragment Names:
Logo.kt
Setup_1.kt
Setup_2.kt
Homepage.kt 
ScreenTime.kt
Schedule.kt
Calendar.kt
Tasks.kt
Setting.kt

Activity xml files:
Logo.xml
Setup_1.xml
Setup_2.xml
Homepage.xml
ScreenTime.xml
Schedule.xml
Calendar.xml
Tasks.xml
Setting.xml
    
App Description: This app will :
-help people cut back on the amount of time they spend using their phone
-organize their day more effectively
-break bad habits


