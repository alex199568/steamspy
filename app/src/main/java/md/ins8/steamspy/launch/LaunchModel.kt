package md.ins8.steamspy.launch

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
import md.ins8.steamspy.update_service.DataUpdateService
import md.ins8.steamspy.update_service.LOCAL_ACTION
import md.ins8.steamspy.update_service.Receiver
import java.util.concurrent.TimeUnit


enum class ModelEvent {
    DONE, DATA_UPDATED
}

interface LaunchModel {
    val eventBus: Observable<ModelEvent>

    fun checkFirstTime(): Boolean
    fun splashWait()
    fun updateData()
}

private val SPLASH_SCREEN_DURATION: Long = 3
private val FIRST_TIME_KEY = "FirstTimeKey"
private val FIRST_TIME_DEFAULT = "FirstTimeDefault"
private val FIRST_TIME_VALUE = "FirstTimeValue"

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
                .subscribe {
                    eventBus.onNext(ModelEvent.DONE)
                }
    }

    override fun updateData() {
        val intent = Intent(context, DataUpdateService::class.java)
        context.startService(intent)


        val receiver = Receiver()
        val broadcastFilter = IntentFilter(LOCAL_ACTION)
        val lbm = LocalBroadcastManager.getInstance(context)
        lbm.registerReceiver(receiver, broadcastFilter)

        receiver.eventBus.subscribe {
            eventBus.onNext(ModelEvent.DATA_UPDATED)
        }
    }
}