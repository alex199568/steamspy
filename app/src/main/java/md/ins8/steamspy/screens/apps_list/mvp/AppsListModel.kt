package md.ins8.steamspy.screens.apps_list.mvp

import md.ins8.steamspy.MinSteamApp
import md.ins8.steamspy.screens.apps_list.AppsListType


class AppsListModel(private val appsListType: AppsListType) {
    fun fetchAppsList(): List<MinSteamApp> {
        val apps: MutableList<MinSteamApp> = mutableListOf()
        val url = "http://cdn.akamai.steamstatic.com/steam/apps/505460/capsule_184x69.jpg"
        val url2 = "http://cdn.akamai.steamstatic.com/steam/apps/305620/capsule_184x69.jpg"
        (1..5).mapTo(apps) { MinSteamApp("app: $appsListType #$it", url) }
        (1..5).mapTo(apps) { MinSteamApp("app: $appsListType 2$it", url2) }
        return apps
    }
}