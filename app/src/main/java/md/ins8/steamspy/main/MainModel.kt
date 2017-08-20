package md.ins8.steamspy.main

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.support.v4.content.LocalBroadcastManager
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import md.ins8.steamspy.update_service.DataUpdateService
import md.ins8.steamspy.update_service.LOCAL_ACTION
import md.ins8.steamspy.update_service.Receiver

enum class ModelEvent {
    ALL_UPDATED
}

interface MainModel {
    val eventBus: Observable<ModelEvent>

    fun updateData()
}

class MainModelImpl(private val context: Context) : MainModel {
    override val eventBus: Subject<ModelEvent> = PublishSubject.create<ModelEvent>()

    private val receiver = Receiver()

    override fun updateData() {
        val intent = Intent(context, DataUpdateService::class.java)
        context.startService(intent)

        val broadcastFilter = IntentFilter(LOCAL_ACTION)
        val lbm = LocalBroadcastManager.getInstance(context)
        lbm.registerReceiver(receiver, broadcastFilter)

        receiver.eventBus.subscribe {
            eventBus.onNext(ModelEvent.ALL_UPDATED)
        }
    }
}