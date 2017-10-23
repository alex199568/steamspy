package md.ins8.steamspy.screens.home

import md.ins8.steamspy.countApps

interface HomeModel {
    val numberOfApps: Long
}

class HomeModelImpl : HomeModel {
    override val numberOfApps: Long
        get() = countApps()
}
