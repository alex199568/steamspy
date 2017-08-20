package md.ins8.steamspy.screens.apps_list.mvp

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import io.realm.Case
import io.realm.Realm
import io.realm.RealmModel
import md.ins8.steamspy.*
import md.ins8.steamspy.screens.apps_list.AppsListType


interface AppsListModel {
    val appsObservable: Observable<List<SteamAppItem>>
    val genreAppsObservable: Observable<List<GenreSteamAppItem>>
    val appsListType: AppsListType

    fun fetchSteamAppItems(searchFor: String = "")
    fun fetchGenreSteamAppItems()
}


class AppsListModelImpl(override val appsListType: AppsListType) : AppsListModel {
    override val appsObservable: Subject<List<SteamAppItem>> = PublishSubject.create<List<SteamAppItem>>()
    override val genreAppsObservable: Subject<List<GenreSteamAppItem>> = PublishSubject.create<List<GenreSteamAppItem>>()

    private val apps: MutableList<SteamAppItem> = mutableListOf()
    private val genreApps: MutableList<GenreSteamAppItem> = mutableListOf()

    private var returningGenreApps = false

    override fun fetchSteamAppItems(searchFor: String) {
        returningGenreApps = false
        fetchAppsList(searchFor)
    }

    override fun fetchGenreSteamAppItems() {
        returningGenreApps = true
        fetchAppsList()
    }

    private fun initLoading(initFunc: () -> Unit) {
        Observable.fromCallable { initFunc() }
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    if (returningGenreApps) {
                        genreAppsObservable.onNext(genreApps)
                    } else {
                        appsObservable.onNext(apps)
                    }
                }
    }

    private fun loadAllApps() {
        val realm = Realm.getDefaultInstance()
        val result = realm.where(RealmSteamApp::class.java)?.findAll()!!
        result.mapTo(apps, { SteamAppItem(it) })
        realm.close()
    }

    private fun loadAppsForName(name: String) {
        val realm = Realm.getDefaultInstance()
        val result = realm.where(RealmSteamApp::class.java).contains("name", name, Case.INSENSITIVE).findAll()
        result.mapTo(apps, { SteamAppItem(it) })
        realm.close()
    }

    inline private fun <reified T> loadTypeApps() where T : RealmModel, T : CustomRealmList {
        val realm = Realm.getDefaultInstance()
        val result = realm.where(T::class.java)?.findAll()!!
        if (returningGenreApps) {
            (result.first() as T).apps.mapTo(genreApps, {
                GenreSteamAppItem(realm.where(RealmSteamApp::class.java)?.equalTo("id", it.appId)?.findFirst()!!)
            })
        } else {
            (result.first() as T).apps.mapTo(apps, {
                SteamAppItem(realm.where(RealmSteamApp::class.java)?.equalTo("id", it.appId)?.findFirst()!!)
            })
        }

        realm.close()
    }

    private fun fetchAppsList(searchFor: String = "") {
        apps.clear()
        when (appsListType) {
            AppsListType.ALL -> {
                if (searchFor.isEmpty()) {
                    initLoading { loadAllApps() }
                } else {
                    initLoading { loadAppsForName(searchFor) }
                }
            }
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
}
