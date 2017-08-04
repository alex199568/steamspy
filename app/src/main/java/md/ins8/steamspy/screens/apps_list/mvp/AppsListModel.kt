package md.ins8.steamspy.screens.apps_list.mvp

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import md.ins8.steamspy.RealmSteamApp
import md.ins8.steamspy.SteamAppItem
import md.ins8.steamspy.app.di.RealmManager
import md.ins8.steamspy.screens.apps_list.AppsListType


interface AppsListModel {
    val appsObservable: Observable<List<SteamAppItem>>

    fun fetchAppsList()
}


class AppsListModelImpl(private val appsListType: AppsListType, private val realmManager: RealmManager) : AppsListModel {
    override val appsObservable: Subject<List<SteamAppItem>> = PublishSubject.create<List<SteamAppItem>>()

    override fun fetchAppsList() {
        if (appsListType == AppsListType.ALL) {
            val apps: MutableList<SteamAppItem> = mutableListOf()
            Observable.fromCallable {
                val result = realmManager.get().where(RealmSteamApp::class.java)?.findAll()!!
                listOf(
                        SteamAppItem(result[3]),
                        SteamAppItem(result[10]),
                        SteamAppItem(result[13]),
                        SteamAppItem(result[23]),
                        SteamAppItem(result[33]),
                        SteamAppItem(result[43]),
                        SteamAppItem(result[223])
                ).forEach { apps.add(it) }
                realmManager.close()
            }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        appsObservable.onNext(apps)
                    }
        } else {
            val apps: MutableList<SteamAppItem> = mutableListOf()
            val url = "http://cdn.akamai.steamstatic.com/steam/apps/505460/capsule_184x69.jpg"
            val url2 = "http://cdn.akamai.steamstatic.com/steam/apps/305620/capsule_184x69.jpg"
            (1..5).mapTo(apps) { SteamAppItem("app: $appsListType #$it", url) }
            (1..5).mapTo(apps) { SteamAppItem("app: $appsListType 2$it", url2) }
            appsObservable.onNext(apps)
        }
    }
}