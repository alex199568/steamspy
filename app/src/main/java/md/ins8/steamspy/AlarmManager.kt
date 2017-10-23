package md.ins8.steamspy

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.preference.PreferenceManager
import md.ins8.steamspy.screens.settings.AUTOMATIC_UPDATE_PREFERENCE_KEY
import md.ins8.steamspy.service.update.DataUpdateService
import md.ins8.steamspy.service.update.TO_CHECK_PARAM_EXTRA
import java.util.*

private const val BOOT_COMPLETED_NAME = "android.intent.action.BOOT_COMPLETED"

private const val SHARED_PREFERENCES_KEY = "data-update-alarm-manager"
private const val ALARM_SETUP_KEY = "AlarmSetupKey"

interface AbstractAlarmManager {
    val alreadySetup: Boolean

    fun setup()
    fun cancel()
}

class DataUpdateAlarmManager(private val context: Context) : AbstractAlarmManager {
    private val pendingIntent: PendingIntent
    private val alarmManager: AlarmManager
    private val dataIntent: Intent = Intent(context, DataUpdateService::class.java).apply {
        putExtra(TO_CHECK_PARAM_EXTRA, true)
    }

    override val alreadySetup: Boolean
        get() = retrievePreferences().getBoolean(ALARM_SETUP_KEY, false)

    init {
        pendingIntent = PendingIntent.getService(context, 0, dataIntent, 0)

        alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    }

    override fun setup() {
        val random = Random()
        val hour = random.nextInt(4)
        val minute = random.nextInt(60)

        val calendar = GregorianCalendar().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.MILLISECOND, 0)
            add(Calendar.DAY_OF_MONTH, 1)
        }

        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, AlarmManager.INTERVAL_DAY, pendingIntent)
        rememberSetup()
    }

    override fun cancel() {
        alarmManager.cancel(pendingIntent)
        forgetSetup()
    }

    private fun rememberSetup() {
        retrievePreferences().edit().putBoolean(ALARM_SETUP_KEY, true).apply()
    }

    private fun forgetSetup() {
        retrievePreferences().edit().putBoolean(ALARM_SETUP_KEY, false).apply()
    }

    private fun retrievePreferences(): SharedPreferences =
            context.getSharedPreferences(SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE)

}

class DataUpdateAlarmBootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action.equals(BOOT_COMPLETED_NAME)) {
            val prefs = PreferenceManager.getDefaultSharedPreferences(context)
            if (prefs.contains(AUTOMATIC_UPDATE_PREFERENCE_KEY) && prefs.getBoolean(AUTOMATIC_UPDATE_PREFERENCE_KEY, false)) {
                if (context != null) {
                    DataUpdateAlarmManager(context).setup()
                }
            }
        }
    }

}


