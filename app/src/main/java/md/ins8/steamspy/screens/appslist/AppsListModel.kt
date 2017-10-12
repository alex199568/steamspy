package md.ins8.steamspy.screens.appslist

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import md.ins8.steamspy.*


interface AppsListModel {
    val appsObservable: Observable<List<RawSteamApp>>

    fun fetchAllApps()
    fun fetchAppsForName(name: String = "")
    fun fetchAppsList(listType: Int)
}


class AppsListModelImpl : AppsListModel {
    override fun fetchAllApps() {
        initLoading {
            loadAllApps()
        }
    }

    override fun fetchAppsForName(name: String) {
        initLoading {
            loadAppsForName(name)
        }
    }

    override fun fetchAppsList(listType: Int) {
        initLoading {
            loadAppsList(listType)
        }
    }

    override val appsObservable: Subject<List<RawSteamApp>> = PublishSubject.create<List<RawSteamApp>>()

    private fun initLoading(loadFunc: () -> List<RawSteamApp>) {
        Observable.fromCallable { loadFunc() }
                .ioToMain()
                .subscribe({
                    appsObservable.onNext(it)
                }, {
                    appsObservable.onNext(listOf())
                })
    }
}
