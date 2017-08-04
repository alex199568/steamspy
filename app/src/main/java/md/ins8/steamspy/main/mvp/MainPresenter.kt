package md.ins8.steamspy.main.mvp

import md.ins8.steamspy.main.NavigationEvent
import md.ins8.steamspy.screens.apps_list.AppsListType


class MainPresenter(private val mainView: MainView, private val mainModel: MainModel) {
    init {
        mainView.navigationEventBus.subscribe {
            when (it) {
                NavigationEvent.HOME -> home()
                NavigationEvent.ALL -> all()
                NavigationEvent.TOP_2_WEEKS -> top2Weeks()
                NavigationEvent.TOP_OWNED -> topOwned()
                NavigationEvent.TOP_TOTAL -> topTotal()
                NavigationEvent.GENRE_ACTION -> genreAction()
                NavigationEvent.GENRE_STRATEGY -> genreStrategy()
                NavigationEvent.GENRE_RPG -> genreRPG()
                NavigationEvent.GENRE_INDIE -> genreIndie()
                NavigationEvent.GENRE_ADVENTURE -> genreAdventure()
                NavigationEvent.GENRE_SPORTS -> genreSports()
                NavigationEvent.GENRE_SIMULATION -> genreSimulation()
                NavigationEvent.GENRE_EARLY_ACCESS -> genreEarlyAccess()
                NavigationEvent.GENRE_EX_EARLY_ACCESS -> genreExEarlyAccess()
                NavigationEvent.GENRE_MMO -> genreMMO()
                NavigationEvent.GENRE_FREE -> genreFree()
                NavigationEvent.NOTIFICATIONS -> notifications()
                NavigationEvent.SETTINGS -> settings()
                NavigationEvent.ABOUT -> about()
            }
        }

        mainView.eventBus.subscribe {
            when (it) {
                ViewEvent.ACTION_UPDATE_DATA -> mainModel.updateData()
            }
        }

        mainModel.eventBus.subscribe {
            when (it) {
                ModelEvent.DATA_DOWNLOADED -> mainView.showDataDownloaded()
                ModelEvent.DATA_UPDATED -> mainView.showDataUpdated()
            }
        }

        home()
    }


    private fun home() {
        mainView.switchToHomeFragment()
    }

    private fun all() {
        mainView.switchToAppsListFragment(AppsListType.ALL)
    }

    private fun top2Weeks() {
        mainView.switchToAppsListFragment(AppsListType.TOP_2_WEEKS)
    }

    private fun topOwned() {
        mainView.switchToAppsListFragment(AppsListType.TOP_OWNED)
    }

    private fun topTotal() {
        mainView.switchToAppsListFragment(AppsListType.TOP_TOTAL)
    }

    private fun genreAction() {
        mainView.switchToAppsListFragment(AppsListType.GENRE_ACTION)
    }

    private fun genreStrategy() {
        mainView.switchToAppsListFragment(AppsListType.GENRE_STRATEGY)
    }

    private fun genreRPG() {
        mainView.switchToAppsListFragment(AppsListType.GENRE_RPG)
    }

    private fun genreIndie() {
        mainView.switchToAppsListFragment(AppsListType.GENRE_INDIE)
    }

    private fun genreAdventure() {
        mainView.switchToAppsListFragment(AppsListType.GENRE_ADVENTURE)
    }

    private fun genreSports() {
        mainView.switchToAppsListFragment(AppsListType.GENRE_SPORTS)
    }

    private fun genreSimulation() {
        mainView.switchToAppsListFragment(AppsListType.GENRE_SIMULATION)
    }

    private fun genreEarlyAccess() {
        mainView.switchToAppsListFragment(AppsListType.GENRE_EARLY_ACCESS)
    }

    private fun genreExEarlyAccess() {
        mainView.switchToAppsListFragment(AppsListType.GENRE_EX_EARLY_ACCESS)
    }

    private fun genreMMO() {
        mainView.switchToAppsListFragment(AppsListType.GENRE_MMO)
    }

    private fun genreFree() {
        mainView.switchToAppsListFragment(AppsListType.GENRE_FREE)
    }

    private fun notifications() {
        mainView.switchToNotificationsFragment()
    }

    private fun settings() {
        mainView.switchToSettingsFragment()
    }

    private fun about() {
        mainView.switchToAboutFragment()
    }
}