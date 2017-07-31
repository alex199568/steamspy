package md.ins8.steamspy.screens.apps_list.mvp

import md.ins8.steamspy.screens.apps_list.AppsListType


class AppsListModel {
    fun fetchData(appsListType: AppsListType): String {
        return when (appsListType) {
            AppsListType.ALL -> "All apps"
            AppsListType.TOP_2_WEEKS -> "Top 2 Weeks"
            AppsListType.TOP_OWNED -> "Top Owned"
            AppsListType.TOP_TOTAL -> "Top total"
            AppsListType.GENRE_ACTION -> "Genre action"
            AppsListType.GENRE_STRATEGY -> "Genre strategy"
            AppsListType.GENRE_RPG -> "Genre rpg"
            AppsListType.GENRE_INDIE -> "Genre indie"
            AppsListType.GENRE_ADVENTURE -> "Genre adventure"
            AppsListType.GENRE_SPORTS -> "Genre sports"
            AppsListType.GENRE_SIMULATION -> "Genre simulation"
            AppsListType.GENRE_EARLY_ACCESS -> "Genre early access"
            AppsListType.GENRE_EX_EARLY_ACCESS -> "Genre ex early access"
            AppsListType.GENRE_MMO -> "Genre mmo"
            AppsListType.GENRE_FREE -> "Genre free"
        }
    }
}