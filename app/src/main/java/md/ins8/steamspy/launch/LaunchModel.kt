package md.ins8.steamspy.launch

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import io.realm.Realm
import md.ins8.steamspy.RealmSteamApp
import md.ins8.steamspy.app.di.SteamSpyAPIService

enum class ModelEvent {
    DONE
}

interface LaunchModel {
    val eventBus: Observable<ModelEvent>
}

class LaunchModelImpl(private val steamSpyAPIService: SteamSpyAPIService, realm: Realm) : LaunchModel {
    override val eventBus: Subject<ModelEvent> = PublishSubject.create<ModelEvent>()

    init {
        steamSpyAPIService.requestTop2Weeks()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map {
                    val result = it.apps
                    realm.beginTransaction()
                    result.forEach {
                        val realmApp = RealmSteamApp(it)
                        realm.copyToRealm(realmApp)
                    }
                    realm.commitTransaction()
                }.observeOn(AndroidSchedulers.mainThread()).subscribe { eventBus.onNext(ModelEvent.DONE) }
    }
}