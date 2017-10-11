package md.ins8.steamspy.screens.appslist.mvp

import android.content.Context
import md.ins8.steamspy.R
import md.ins8.steamspy.RawSteamApp
import md.ins8.steamspy.details.startAppDetailsActivity

class AppsListPresenter(private val model: AppsListModel, private val view: AppsListView,
                        private val context: Context,
                        private val listType: Int, private val listTypeId: Int,
                        private val searchParam: String = "") {
    private val noAppsMessage = context.getString(R.string.noAppsMessage)

    init {
        view.eventBus.subscribe {
            when (it) {
                AppsListViewEvent.VIEW_CREATED -> onViewCreated()
            }
        }

        view.itemClickObservable.subscribe { startAppDetailsActivity(view.appsListContext, it) }

        model.appsObservable.subscribe {
            when (listType) {
                AppsListType.DEFAULT.listTypeId -> handleDefaultListType(it)
                AppsListType.GENRE.listTypeId -> handleGenreListType(it)
                AppsListType.TOP.listTypeId -> handleTopListType(it)
            }
        }
    }

    private fun handleDefaultListType(apps: List<RawSteamApp>) {
        if (searchParam.isEmpty()) {
            if (apps.isEmpty()) {
                view.showEmptyList(noAppsMessage)
            } else {
                view.showAppsList(apps)
            }
        } else {
            if (apps.isEmpty()) {
                view.showEmptyList(noAppsMessage)
            } else {
                view.showAppsList(apps)
            }
        }
    }

    private fun handleGenreListType(apps: List<RawSteamApp>) {
        if (apps.isEmpty()) {
            view.showEmptyList(noAppsMessage)
        } else {
            view.showGenreAppsList(apps)
        }
    }

    private fun handleTopListType(apps: List<RawSteamApp>) {
        if (apps.isEmpty()) {
            view.showEmptyList(noAppsMessage)
        } else {
            view.showTopAppsList(apps)
        }
    }

    private fun onViewCreated() {
        if (listType == AppsListType.GENRE.listTypeId || listType == AppsListType.TOP.listTypeId) {
            model.fetchAppsList(listTypeId)
        } else if (!searchParam.isEmpty()) {
            model.fetchAppsForName(searchParam)
        } else {
            model.fetchAllApps()
        }
    }
}
