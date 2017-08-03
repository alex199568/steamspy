package md.ins8.steamspy.main

import md.ins8.steamspy.R

enum class NavigationEvent(val titleStrRes: Int) {
    HOME(R.string.titleHome),
    ALL(R.string.titleAll),
    TOP_2_WEEKS(R.string.titleTop2Weeks),
    TOP_OWNED(R.string.titleTopOwned),
    TOP_TOTAL(R.string.titleTopTotal),
    GENRE_ACTION(R.string.titleGenreAction),
    GENRE_STRATEGY(R.string.titleGenreStrategy),
    GENRE_RPG(R.string.titleGenreRPG),
    GENRE_INDIE(R.string.titleGenreIndie),
    GENRE_ADVENTURE(R.string.titleGenreAdventure),
    GENRE_SPORTS(R.string.titleGenreSports),
    GENRE_SIMULATION(R.string.titleGenreSimulation),
    GENRE_EARLY_ACCESS(R.string.titleGenreEarlyAccess),
    GENRE_EX_EARLY_ACCESS(R.string.titleGenreExEarlyAccess),
    GENRE_MMO(R.string.titleGenreMMO),
    GENRE_FREE(R.string.titleGenreFree),
    NOTIFICATIONS(R.string.titleNotifications),
    SETTINGS(R.string.titleSettings),
    ABOUT(R.string.titleAbout)
}
