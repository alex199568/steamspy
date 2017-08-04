package md.ins8.steamspy.main.mvp

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import java.util.concurrent.TimeUnit

enum class ModelEvent {
    DATA_DOWNLOADED, DATA_UPDATED
}

interface MainModel {
    val eventBus: Observable<ModelEvent>

    fun updateData()
}

class MainModelImpl : MainModel {
    override val eventBus: Subject<ModelEvent> = PublishSubject.create<ModelEvent>()

    override fun updateData() {
        createDataDownloadObservable()
                .subscribe {
                    eventBus.onNext(ModelEvent.DATA_DOWNLOADED)
                    createDataUpdateObservable()
                            .subscribe {
                                eventBus.onNext(ModelEvent.DATA_UPDATED)
                            }
                }
    }

    private fun createDataDownloadObservable(): Observable<Boolean> {
        return Observable.just(true).delay(2, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    private fun createDataUpdateObservable(): Observable<Boolean> {
        return Observable.just(true).delay(1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }
}