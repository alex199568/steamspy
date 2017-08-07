package md.ins8.steamspy.screens.apps_list.mvp

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import md.ins8.steamspy.RealmGenreFree
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
                val realm = realmManager.create()
                val result = realm.where(RealmSteamApp::class.java)?.findAll()!!
                result.mapTo(apps, { SteamAppItem(it) })
                realm.close()
            }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        appsObservable.onNext(apps)
                    }
        } else if (appsListType == AppsListType.GENRE_FREE) {
            val apps: MutableList<SteamAppItem> = mutableListOf()
            Observable.fromCallable {
                val realm = realmManager.create()
                val genreResult = realm.where(RealmGenreFree::class.java)?.findAll()!!
                genreResult.first().apps.mapTo(apps, {
                    SteamAppItem(realm.where(RealmSteamApp::class.java)?.equalTo("id", it.appId)?.findFirst()!!)
                })
                realm.close()
            }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        appsObservable.onNext(apps)
                    }
        }
    }
}