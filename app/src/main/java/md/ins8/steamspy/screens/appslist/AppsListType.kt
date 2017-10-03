package md.ins8.steamspy.screens.appslist

import md.ins8.steamspy.main.NavigationEvent

enum class AppsListType(val navigation: NavigationEvent) {
    ALL(NavigationEvent.ALL),
    TOP_2_WEEKS(NavigationEvent.TOP_2_WEEKS),
    TOP_OWNED(NavigationEvent.TOP_OWNED),
    TOP_TOTAL(NavigationEvent.TOP_TOTAL),
    GENRE_ACTION(NavigationEvent.GENRE_ACTION),
    GENRE_STRATEGY(NavigationEvent.GENRE_STRATEGY),
    GENRE_RPG(NavigationEvent.GENRE_RPG),
    GENRE_INDIE(NavigationEvent.GENRE_INDIE),
    GENRE_ADVENTURE(NavigationEvent.GENRE_ADVENTURE),
    GENRE_SPORTS(NavigationEvent.GENRE_SPORTS),
    GENRE_SIMULATION(NavigationEvent.GENRE_SIMULATION),
    GENRE_EARLY_ACCESS(NavigationEvent.GENRE_EARLY_ACCESS),
    GENRE_EX_EARLY_ACCESS(NavigationEvent.GENRE_EX_EARLY_ACCESS),
    GENRE_MMO(NavigationEvent.GENRE_MMO),
    GENRE_FREE(NavigationEvent.GENRE_FREE)
}