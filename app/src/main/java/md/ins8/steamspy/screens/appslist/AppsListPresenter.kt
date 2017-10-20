package md.ins8.steamspy.screens.appslist

import android.content.Context
import io.realm.RealmResults
import md.ins8.steamspy.R
import md.ins8.steamspy.RealmSteamApp
import md.ins8.steamspy.details.startAppDetailsActivity
import timber.log.Timber

class AppsListPresenter(private val model: AppsListModel, private val view: AppsListView,
                        private val context: Context,
                        private val listType: Int, private val listTypeId: Int,
                        private val searchParam: String = "") {
    private val noAppsMessage = context.getString(R.string.noAppsMessage)
    private val noSearchResultsMessage = context.getString(R.string.noSearchResultsMessage)

    init {
        model.start()

        view.eventBus.subscribe {
            when (it) {
                AppsListViewEvent.VIEW_CREATED -> {
                    fetchApps()
                }
                AppsListViewEvent.VIEW_DESTROYED -> model.end()
            }
        }

        view.itemClickObservable.subscribe { startAppDetailsActivity(view.appsListContext, it) }

        model.appsObservable.subscribe({
            when (listType) {
                AppsListType.DEFAULT.listTypeId -> handleDefaultListType(it)
                AppsListType.GENRE.listTypeId -> handleGenreListType(it)
                AppsListType.TOP.listTypeId -> handleTopListType(it)
            }
        }, {
            Timber.e(it)
            view.showEmptyList(noAppsMessage)
        })
    }

    private fun handleDefaultListType(apps: RealmResults<RealmSteamApp>) {
        if (searchParam.isEmpty()) {
            if (apps.isEmpty()) {
                view.showEmptyList(noAppsMessage)
            } else {
                view.showAppsList(apps)
            }
        } else {
            if (apps.isEmpty()) {
                view.showEmptyList("$noSearchResultsMessage: $searchParam")
            } else {
                view.showAppsList(apps)
            }
        }
    }

    private fun handleGenreListType(apps: RealmResults<RealmSteamApp>) {
        if (apps.isEmpty()) {
            view.showEmptyList(noAppsMessage)
        } else {
            view.showGenreAppsList(apps)
        }
    }

    private fun handleTopListType(apps: RealmResults<RealmSteamApp>) {
        if (apps.isEmpty()) {
            view.showEmptyList(noAppsMessage)
        } else {
            view.showTopAppsList(apps)
        }
    }

    private fun fetchApps() {
        if (listType == AppsListType.GENRE.listTypeId || listType == AppsListType.TOP.listTypeId) {
            model.fetchAppsList(listTypeId)
        } else if (!searchParam.isEmpty()) {
            model.fetchAppsForName(searchParam)
        } else {
            model.fetchAllApps()
        }
    }
}
