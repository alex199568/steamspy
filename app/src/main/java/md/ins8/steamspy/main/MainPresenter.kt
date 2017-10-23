package md.ins8.steamspy.main

import android.content.Context
import md.ins8.steamspy.*


class MainPresenter(private val mainView: MainView, private val mainModel: MainModel, private val context: Context) {
    private var lastSearchInput = ""

    init {
        mainView.navigationEventBus.subscribe {
            when (it) {
                NavigationEvent.HOME -> home()
                NavigationEvent.ALL -> appsList(ListTypes.ALL.listType)
                NavigationEvent.TOP_2_WEEKS -> appsList(TopListTypes.TOP_2_WEEKS.listType)
                NavigationEvent.TOP_OWNED -> appsList(TopListTypes.TOP_OWNED.listType)
                NavigationEvent.TOP_TOTAL -> appsList(TopListTypes.TOP_TOTAL.listType)
                NavigationEvent.GENRE_ACTION -> appsList(GenreListTypes.GENRE_ACTION.listType)
                NavigationEvent.GENRE_STRATEGY -> appsList(GenreListTypes.GENRE_STRATEGY.listType)
                NavigationEvent.GENRE_RPG -> appsList(GenreListTypes.GENRE_RPG.listType)
                NavigationEvent.GENRE_INDIE -> appsList(GenreListTypes.GENRE_INDIE.listType)
                NavigationEvent.GENRE_ADVENTURE -> appsList(GenreListTypes.GENRE_ADVENTURE.listType)
                NavigationEvent.GENRE_SPORTS -> appsList(GenreListTypes.GENRE_SPORTS.listType)
                NavigationEvent.GENRE_SIMULATION -> appsList(GenreListTypes.GENRE_SIMULATION.listType)
                NavigationEvent.GENRE_EARLY_ACCESS -> appsList(GenreListTypes.GENRE_EARLY_ACCESS.listType)
                NavigationEvent.GENRE_EX_EARLY_ACCESS -> appsList(GenreListTypes.GENRE_EX_EARLY_ACCESS.listType)
                NavigationEvent.GENRE_MMO -> appsList(GenreListTypes.GENRE_MMO.listType)
                NavigationEvent.GENRE_FREE -> appsList(GenreListTypes.GENRE_FREE.listType)
                NavigationEvent.SETTINGS -> settings()
                NavigationEvent.ABOUT -> about()
            }
        }

        mainView.eventBus.subscribe {
            when (it) {
                ViewEvent.ACTION_UPDATE_DATA -> {
                    if (!onWifi(context)) {
                        mainView.confirmUpdate()
                    } else {
                        mainModel.updateData()
                    }
                }
                ViewEvent.ACTION_SEARCH -> {
                    mainView.showInputDialog()
                    mainView.inputEvents.subscribe {
                        if (it != lastSearchInput) {
                            lastSearchInput = it
                            mainView.switchToAppsListFragment(it)
                        }
                    }
                }
                ViewEvent.UPDATE_CONFIRMED -> mainModel.updateData()
            }
        }

        mainModel.eventBus.subscribe {
            if (it == ModelEvent.ALL_UPDATED) {
                mainView.refreshListFragment()
            }
        }

        if (mainModel.appsEmpty) {
            emptyHome()
        } else {
            home()
        }
    }

    private fun emptyHome() {
        mainView.switchToEmptyHomeFragment()
    }

    private fun home() {
        mainView.switchToHomeFragment()
    }

    private fun appsList(listType: ListType) {
        mainView.switchToAppsListFragment(listType)
    }

    private fun about() {
        mainView.switchToAboutFragment()
    }

    private fun settings() {
        mainView.switchToSettingsFragment()
    }
}