package md.ins8.steamspy

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.preference.PreferenceManager
import md.ins8.steamspy.screens.settings.AUTOMATIC_UPDATE_PREFERENCE_KEY
import md.ins8.steamspy.service.update.DataUpdateService
import java.util.*

private const val BOOT_COMPLETED_NAME = "android.intent.action.BOOT_COMPLETED"

interface AbstractAlarmManager {
    val alreadySetup: Boolean

    fun setup()
    fun cancel()
}

class DataUpdateAlarmManager(private val context: Context) : AbstractAlarmManager {
    private val pendingIntent: PendingIntent
    private val alarmManager: AlarmManager
    private val dataIntent: Intent = Intent(context, DataUpdateService::class.java)

    override val alreadySetup: Boolean
        get() = PendingIntent.getService(context, 0, dataIntent, PendingIntent.FLAG_NO_CREATE) == null

    init {
        pendingIntent = PendingIntent.getService(context, 0, dataIntent, 0)

        alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    }

    override fun setup() {
        val random = Random()

        val calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        calendar.set(Calendar.HOUR_OF_DAY, random.nextInt(4))
        calendar.set(Calendar.MINUTE, random.nextInt(60))

        alarmManager.setInexactRepeating(AlarmManager.RTC, calendar.timeInMillis, AlarmManager.INTERVAL_DAY, pendingIntent)
    }

    override fun cancel() {
        alarmManager.cancel(pendingIntent)
    }

    inner class DataUpdateAlarmBootReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action.equals(BOOT_COMPLETED_NAME)) {
                val prefs = PreferenceManager.getDefaultSharedPreferences(context)
                if (prefs.contains(AUTOMATIC_UPDATE_PREFERENCE_KEY) && prefs.getBoolean(AUTOMATIC_UPDATE_PREFERENCE_KEY, false)) {
                    setup()
                }
            }
        }
    }
}

