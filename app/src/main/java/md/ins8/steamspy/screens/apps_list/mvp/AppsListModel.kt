package md.ins8.steamspy.screens.apps_list.mvp

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import io.realm.RealmModel
import md.ins8.steamspy.*
import md.ins8.steamspy.app.di.RealmManager
import md.ins8.steamspy.screens.apps_list.AppsListType


interface AppsListModel {
    val appsObservable: Observable<List<SteamAppItem>>

    fun fetchAppsList()
}


class AppsListModelImpl(private val appsListType: AppsListType, private val realmManager: RealmManager) : AppsListModel {
    override val appsObservable: Subject<List<SteamAppItem>> = PublishSubject.create<List<SteamAppItem>>()

    private val apps: MutableList<SteamAppItem> = mutableListOf()

    override fun fetchAppsList() {
        apps.clear()
        when (appsListType) {
            AppsListType.ALL -> initLoading { loadAllApps() }
            AppsListType.TOP_2_WEEKS -> initLoading { loadTypeApps<RealmTop2Weeks>() }
            AppsListType.TOP_OWNED -> initLoading { loadTypeApps<RealmTopOwned>() }
            AppsListType.TOP_TOTAL -> initLoading { loadTypeApps<RealmTopTotal>() }
            AppsListType.GENRE_ACTION -> initLoading { loadTypeApps<RealmGenreAction>() }
            AppsListType.GENRE_STRATEGY -> initLoading { loadTypeApps<RealmGenreStrategy>() }
            AppsListType.GENRE_RPG -> initLoading { loadTypeApps<RealmGenreRPG>() }
            AppsListType.GENRE_INDIE -> initLoading { loadTypeApps<RealmGenreIndie>() }
            AppsListType.GENRE_ADVENTURE -> initLoading { loadTypeApps<RealmGenreAdventure>() }
            AppsListType.GENRE_SPORTS -> initLoading { loadTypeApps<RealmGenreSports>() }
            AppsListType.GENRE_SIMULATION -> initLoading { loadTypeApps<RealmGenreSports>() }
            AppsListType.GENRE_EARLY_ACCESS -> initLoading { loadTypeApps<RealmGenreSimulation>() }
            AppsListType.GENRE_EX_EARLY_ACCESS -> initLoading { loadTypeApps<RealmGenreExEarlyAccess>() }
            AppsListType.GENRE_MMO -> initLoading { loadTypeApps<RealmGenreMMO>() }
            AppsListType.GENRE_FREE -> initLoading { loadTypeApps<RealmGenreFree>() }
        }
    }

    private fun initLoading(initFunc: () -> Unit) {
        Observable.fromCallable { initFunc() }
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe { appsObservable.onNext(apps) }
    }

    private fun loadAllApps() {
        val realm = realmManager.create()
        val result = realm.where(RealmSteamApp::class.java)?.findAll()!!
        result.mapTo(apps, { SteamAppItem(it) })
        realm.close()
    }

    inline private fun <reified T> loadTypeApps() where T : RealmModel, T : CustomRealmList {
        val realm = realmManager.create()
        val genreResult = realm.where(T::class.java)?.findAll()!!
        (genreResult.first() as T).apps.mapTo(apps, {
            SteamAppItem(realm.where(RealmSteamApp::class.java)?.equalTo("id", it.appId)?.findFirst()!!)
        })
        realm.close()
    }
}
