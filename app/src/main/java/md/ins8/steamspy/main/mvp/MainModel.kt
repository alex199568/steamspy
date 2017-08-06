package md.ins8.steamspy.main.mvp

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import md.ins8.steamspy.app.di.RealmManager
import md.ins8.steamspy.app.di.SteamSpyAPIService

enum class ModelEvent {
    DATA_DOWNLOADED, DATA_UPDATED
}

interface MainModel {
    val eventBus: Observable<ModelEvent>

    fun updateData()
}

class MainModelImpl(private val steamAppsAPIService: SteamSpyAPIService, private val realmManager: RealmManager) : MainModel {
    override val eventBus: Subject<ModelEvent> = PublishSubject.create<ModelEvent>()

    override fun updateData() {
        steamAppsAPIService.requestAll().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe { data ->
                    eventBus.onNext(ModelEvent.DATA_DOWNLOADED)
                    Observable.fromCallable {
                        realmManager.delete()
                        val realm = realmManager.create()
                        realm.executeTransaction {
                            data.toRealm().forEach { realm.copyToRealm(it) }
                        }
                        realmManager.close()
                    }.subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe {
                                eventBus.onNext(ModelEvent.DATA_UPDATED)
                            }

                }
    }
}