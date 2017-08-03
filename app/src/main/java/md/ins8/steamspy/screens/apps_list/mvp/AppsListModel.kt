package md.ins8.steamspy.screens.apps_list.mvp

import io.realm.Realm
import md.ins8.steamspy.RealmSteamApp
import md.ins8.steamspy.SteamAppItem
import md.ins8.steamspy.screens.apps_list.AppsListType


class AppsListModel(private val appsListType: AppsListType, private val realm: Realm) {
    fun fetchAppsList(): List<SteamAppItem> {
        if (appsListType == AppsListType.TOP_2_WEEKS) {
            val result = realm.where(RealmSteamApp::class.java).findAll()
            val apps: MutableList<SteamAppItem> = mutableListOf()
            result.mapTo(apps, { SteamAppItem(it) })
            return apps
        } else {
            val apps: MutableList<SteamAppItem> = mutableListOf()
            val url = "http://cdn.akamai.steamstatic.com/steam/apps/505460/capsule_184x69.jpg"
            val url2 = "http://cdn.akamai.steamstatic.com/steam/apps/305620/capsule_184x69.jpg"
            (1..5).mapTo(apps) { SteamAppItem("app: $appsListType #$it", url) }
            (1..5).mapTo(apps) { SteamAppItem("app: $appsListType 2$it", url2) }
            return apps
        }
    }
}