package md.ins8.steamspy.details

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import io.realm.Realm
import md.ins8.steamspy.RawSteamApp
import md.ins8.steamspy.RealmSteamApp
import timber.log.Timber

interface AppDetailsModel {
    val appLoadedObservable: Observable<RawSteamApp>
    var viewExpanded: Boolean

    fun loadApp()
}

class AppDetailsModelImpl(private val appId: Long) : AppDetailsModel {
    override val appLoadedObservable: Subject<RawSteamApp> = PublishSubject.create<RawSteamApp>()

    override var viewExpanded: Boolean = false

    override fun loadApp() {
        var app: RawSteamApp? = null

        Observable.fromCallable {
            val realm = Realm.getDefaultInstance()
            val result = realm.where(RealmSteamApp::class.java).equalTo("id", appId).findFirst()
            app = RawSteamApp(result)
            realm.close()
        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    appLoadedObservable.onNext(app!!)
                }, {
                    Timber.e(it)
                    appLoadedObservable.onNext(RawSteamApp())
                })
    }
}