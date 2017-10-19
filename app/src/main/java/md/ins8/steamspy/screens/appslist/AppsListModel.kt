package md.ins8.steamspy.screens.appslist

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import io.realm.Realm
import io.realm.RealmResults
import md.ins8.steamspy.RealmSteamApp
import md.ins8.steamspy.loadAllApps
import md.ins8.steamspy.loadAppsForName
import md.ins8.steamspy.loadAppsList

interface AppsListModel {
    val appsObservable: Observable<RealmResults<RealmSteamApp>>

    fun fetchAllApps()
    fun fetchAppsForName(name: String = "")
    fun fetchAppsList(listType: Int)

    fun start()
    fun end()
}


class AppsListModelImpl : AppsListModel {
    private lateinit var realm: Realm

    override fun start() {
        realm = Realm.getDefaultInstance()
    }

    override fun end() {
        realm.close()
    }

    override fun fetchAllApps() {
        initLoading {
            loadAllApps(realm)
        }
    }

    override fun fetchAppsForName(name: String) {
        initLoading {
            loadAppsForName(realm, name)
        }
    }

    override fun fetchAppsList(listType: Int) {
        initLoading {
            loadAppsList(realm, listType)
        }
    }

    override val appsObservable: Subject<RealmResults<RealmSteamApp>> = PublishSubject.create<RealmResults<RealmSteamApp>>()

    private fun initLoading(loadFunc: () -> RealmResults<RealmSteamApp>) {
        Observable.fromCallable { loadFunc() }
                .subscribe({
                    appsObservable.onNext(it)
                }, {
                    appsObservable.onError(it)
                })
    }
}
