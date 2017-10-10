package md.ins8.steamspy.screens.appslist.mvp

import md.ins8.steamspy.GenreListTypes
import md.ins8.steamspy.ListType
import md.ins8.steamspy.R
import md.ins8.steamspy.TopListTypes
import md.ins8.steamspy.details.startAppDetailsActivity
import md.ins8.steamspy.screens.appslist.fragment.AppsListView
import md.ins8.steamspy.screens.appslist.fragment.AppsListViewEvent


class AppsListPresenter(private val model: AppsListModel, private val view: AppsListView) {
    init {
        view.eventBus.subscribe {
            when (it) {
                AppsListViewEvent.VIEW_CREATED -> onViewCreated()
            }
        }

        view.itemClickObservable.subscribe { startAppDetailsActivity(view.appsListContext, it) }

        model.appsObservable.subscribe {
            val listType = ListType(-1, R.string.app_name, "tempListType")
            if (!TopListTypes.values().filter { it.listType == listType }.isEmpty()) {
                // view.showListAsTopApps(it)
            } else if (!GenreListTypes.values().filter { it.listType == listType }.isEmpty()) {
                // view.showListAsGenreApps(it)
            } else {
                // view.showAppsList(it)
            }
        }
    }

    private fun onViewCreated() {
        if (/* list type is genre or top */true) {
            val listType = ListType(-1, R.string.app_name, "tempListType")
            model.fetchAppsList(listType)
        } else if (/* search parameter is not empty */false) {
            val searchParam = "tempTestParam"
            model.fetchAppsForName(searchParam)
        } else {
            model.fetchAllApps()
        }
    }
}
