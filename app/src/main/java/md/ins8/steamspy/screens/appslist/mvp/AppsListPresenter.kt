package md.ins8.steamspy.screens.appslist.mvp

import md.ins8.steamspy.details.startAppDetailsActivity
import md.ins8.steamspy.screens.appslist.AppsListType
import md.ins8.steamspy.screens.appslist.fragment.AppsListView
import md.ins8.steamspy.screens.appslist.fragment.AppsListViewEvent


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
                if (it.isEmpty()) {
                    view.showEmptyList("Looks like the genre is dead, things happen...")
                } else {
                    view.showGenreAppsList(it)
                }
            }
        } else {
            model.fetchSteamAppItems(searchFor)
            model.appsObservable.subscribe {
                if (it.isEmpty()) {
                    view.showEmptyList("There are no apps which match the name '$searchFor'")
                } else {
                    view.showAppsList(it)
                }
            }
        }
    }
}
