package md.ins8.steamspy.launch

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.support.v4.content.LocalBroadcastManager
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import md.ins8.steamspy.R
import md.ins8.steamspy.service.update.DataUpdateService
import md.ins8.steamspy.service.update.LOCAL_ACTION
import md.ins8.steamspy.service.update.Receiver
import timber.log.Timber
import java.util.*
import java.util.concurrent.TimeUnit


enum class ModelEvent {
    DONE, DATA_UPDATED
}

interface LaunchModel {
    val eventBus: Observable<ModelEvent>

    fun checkFirstTime(): Boolean
    fun splashWait()
    fun updateData()
    fun setupUpdate()
}

private const val SPLASH_SCREEN_DURATION: Long = 3
private const val FIRST_TIME_KEY = "FirstTimeKey"
private const val FIRST_TIME_DEFAULT = "FirstTimeDefault"
private const val FIRST_TIME_VALUE = "FirstTimeValue"
private const val BOOT_COMPLETED_NAME = "android.intent.action.BOOT_COMPLETED"

class LaunchModelImpl(private val context: Context) : LaunchModel {
    override val eventBus: Subject<ModelEvent> = PublishSubject.create<ModelEvent>()

    override fun checkFirstTime(): Boolean {
        val sharedPrefs = context.getSharedPreferences(context.getString(R.string.sharedPreferencesFileKey), Context.MODE_PRIVATE)
        val stored = sharedPrefs.getString(FIRST_TIME_KEY, FIRST_TIME_DEFAULT)
        if (stored == FIRST_TIME_VALUE) {
            return false
        }
        sharedPrefs.edit().putString(FIRST_TIME_KEY, FIRST_TIME_VALUE).apply()
        return true
    }

    override fun splashWait() {
        Observable.just(true).delay(SPLASH_SCREEN_DURATION, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    eventBus.onNext(ModelEvent.DONE)
                }, {
                    Timber.e(it)
                })
    }

    override fun updateData() {
        context.startService(Intent(context, DataUpdateService::class.java))

        val receiver = Receiver()
        val broadcastFilter = IntentFilter(LOCAL_ACTION)
        val lbm = LocalBroadcastManager.getInstance(context)
        lbm.registerReceiver(receiver, broadcastFilter)

        receiver.eventBus.subscribe { eventBus.onNext(ModelEvent.DATA_UPDATED) }
    }

    override fun setupUpdate() = setupDataUpdateAlarm(context)
}

class DataUpdateAlarmBootReceiver : BroadcastReceiver() {
    override fun onReceive(p0: Context?, p1: Intent?) {
        if (p1?.action.equals(BOOT_COMPLETED_NAME)) {
            setupDataUpdateAlarm(p0!!)
        }
    }

}

fun setupDataUpdateAlarm(context: Context) {
    val random = Random()

    val calendar = Calendar.getInstance()
    calendar.timeInMillis = System.currentTimeMillis()
    calendar.set(Calendar.HOUR_OF_DAY, random.nextInt(4))
    calendar.set(Calendar.MINUTE, random.nextInt(60))

    val dataIntent = Intent(context, DataUpdateService::class.java)

    val pendingIntent = PendingIntent.getService(context, 0, dataIntent, 0)

    val alarmManager: AlarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    alarmManager.setInexactRepeating(AlarmManager.RTC, calendar.timeInMillis, AlarmManager.INTERVAL_DAY, pendingIntent)
}