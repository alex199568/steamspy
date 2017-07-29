package md.ins8.steamspy.main.mvp

import md.ins8.steamspy.main.NavigationEvent


class MainPresenter(mainView: MainView) {
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
    }

    private fun home() {

    }

    private fun all() {

    }

    private fun top2Weeks() {

    }

    private fun topOwned() {

    }

    private fun topTotal() {

    }

    private fun genreAction() {

    }

    private fun genreStrategy() {

    }

    private fun genreRPG() {

    }

    private fun genreIndie() {

    }

    private fun genreAdventure() {

    }

    private fun genreSports() {

    }

    private fun genreSimulation() {

    }

    private fun genreEarlyAccess() {

    }

    private fun genreExEarlyAccess() {

    }

    private fun genreMMO() {

    }

    private fun genreFree() {

    }

    private fun notifications() {

    }

    private fun settings() {

    }

    private fun about() {

    }
}