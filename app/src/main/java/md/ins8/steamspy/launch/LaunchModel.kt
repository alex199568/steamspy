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
import md.ins8.steamspy.service.update.DataUpdateService
import md.ins8.steamspy.service.update.LOCAL_ACTION
import md.ins8.steamspy.service.update.Receiver
import timber.log.Timber
import java.util.concurrent.TimeUnit


enum class ModelEvent {
    DONE, DATA_UPDATED
}

interface LaunchModel {
    val eventBus: Observable<ModelEvent>

    fun splashWait()
    fun updateData()
}

private const val SPLASH_SCREEN_DURATION: Long = 3

class LaunchModelImpl(private val context: Context) : LaunchModel {
    override val eventBus: Subject<ModelEvent> = PublishSubject.create<ModelEvent>()

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
}

