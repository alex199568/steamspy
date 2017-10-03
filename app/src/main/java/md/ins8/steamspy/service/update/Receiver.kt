package md.ins8.steamspy.service.update

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject

enum class DataUpdateEvent {
    DONE
}

interface DataUpdateServiceReceiver {
    val eventBus: Observable<DataUpdateEvent>
}

const val LOCAL_ACTION = "md.ins8.steamspy.update_service.DataUpdateEvent.DONE"


class Receiver : BroadcastReceiver(), DataUpdateServiceReceiver {
    override val eventBus: Subject<DataUpdateEvent> = PublishSubject.create<DataUpdateEvent>()

    override fun onReceive(context: Context?, intent: Intent?) {
        eventBus.onNext(DataUpdateEvent.DONE)
    }
}
