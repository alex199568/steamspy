package md.ins8.steamspy.launch

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import java.util.concurrent.TimeUnit

enum class ModelEvent {
    DONE
}

interface LaunchModel {
    val eventBus: Observable<ModelEvent>
}

private val SPLASH_SCREEN_DURATION: Long = 3

class LaunchModelImpl : LaunchModel {
    override val eventBus: Subject<ModelEvent> = PublishSubject.create<ModelEvent>()

    init {
        Observable.just(true).delay(SPLASH_SCREEN_DURATION, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    eventBus.onNext(ModelEvent.DONE)
                }
    }
}