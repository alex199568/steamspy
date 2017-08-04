package md.ins8.steamspy.launch

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import md.ins8.steamspy.app.di.SteamSpyAPIService
import java.util.concurrent.TimeUnit

enum class ModelEvent {
    DONE
}

interface LaunchModel {
    val eventBus: Observable<ModelEvent>
}

class LaunchModelImpl(private val steamSpyAPIService: SteamSpyAPIService) : LaunchModel {
    override val eventBus: Subject<ModelEvent> = PublishSubject.create<ModelEvent>()

    init {
        Observable.just(true).delay(1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    eventBus.onNext(ModelEvent.DONE)
                }
    }
}