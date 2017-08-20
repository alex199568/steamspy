package md.ins8.steamspy.screens.apps_list.mvp

import md.ins8.steamspy.app_details.startAppDetailsActivity
import md.ins8.steamspy.screens.apps_list.AppsListType
import md.ins8.steamspy.screens.apps_list.fragment.AppsListView
import md.ins8.steamspy.screens.apps_list.fragment.AppsListViewEvent


class AppsListPresenter(private val model: AppsListModel, private val view: AppsListView) {
    var searchFor = ""

    init {
        view.eventBus.subscribe {
            when (it) {
                AppsListViewEvent.VIEW_CREATED -> onViewCreated()
            }
        }

        view.itemClickObservable.subscribe { startAppDetailsActivity(view.appsListContext, it) }
    }

    private fun onViewCreated() {
        if (listOf(
                AppsListType.GENRE_ACTION,
                AppsListType.GENRE_ADVENTURE,
                AppsListType.GENRE_EARLY_ACCESS,
                AppsListType.GENRE_EX_EARLY_ACCESS,
                AppsListType.GENRE_FREE,
                AppsListType.GENRE_INDIE,
                AppsListType.GENRE_MMO,
                AppsListType.GENRE_RPG,
                AppsListType.GENRE_SIMULATION,
                AppsListType.GENRE_SPORTS,
                AppsListType.GENRE_STRATEGY
        ).contains(model.appsListType)) {
            model.fetchGenreSteamAppItems()
            model.genreAppsObservable.subscribe {
                view.showGenreAppsList(it)
            }
        } else {
            model.fetchSteamAppItems(searchFor)
            model.appsObservable.subscribe {
                view.showAppsList(it)
            }
        }
    }
}
