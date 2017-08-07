package md.ins8.steamspy

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

data class Tag(
        var name: String = "",
        var votes: Int = 0
) {
    constructor(realmTag: RealmTag) : this(realmTag.name, realmTag.votes)
}

data class RawSteamApp(
        var id: Long = -1,
        var name: String = "",
        var dev: MutableList<String> = mutableListOf(),
        var pub: MutableList<String> = mutableListOf(),
        var rank: Int = 0,
        var numOwners: Int = 0,
        var ownersVariance: Int = 0,
        var playersTotal: Int = 0,
        var playersTotalVariance: Int = 0,
        var players2Weeks: Int = 0,
        var players2WeeksVariance: Int = 0,
        var averageTotal: Int = 0,
        var average2Weeks: Int = 0,
        var medianTotal: Int = 0,
        var median2Weeks: Int = 0,
        var ccu: Int = 0,
        var price: String = "0",
        var tags: MutableList<Tag> = mutableListOf()
) {
    constructor(realmSteamApp: RealmSteamApp) : this(
            id = realmSteamApp.id,
            name = realmSteamApp.name,
            rank = realmSteamApp.rank,
            numOwners = realmSteamApp.numOwners,
            ownersVariance = realmSteamApp.ownersVariance,
            playersTotal = realmSteamApp.playersTotal,
            playersTotalVariance = realmSteamApp.playersTotalVariance,
            players2Weeks = realmSteamApp.players2Weeks,
            players2WeeksVariance = realmSteamApp.players2WeeksVariance,
            averageTotal = realmSteamApp.averageTotal,
            average2Weeks = realmSteamApp.average2Weeks,
            medianTotal = realmSteamApp.medianTotal,
            median2Weeks = realmSteamApp.median2Weeks,
            ccu = realmSteamApp.ccu,
            price = realmSteamApp.price
    ) {
        realmSteamApp.dev.mapTo(dev, { it.name })
        realmSteamApp.pub.mapTo(pub, { it.name })
        realmSteamApp.tags.mapTo(tags, { Tag(it) })
    }
}

private val IMAGE_CAPSULE_START_URL = "http://cdn.akamai.steamstatic.com/steam/apps/"
private val IMAGE_CAPSULE_END_URL = "/capsule_184x69.jpg"

data class SteamAppItem(
        val name: String,
        var imgUrl: String = ""
) {
    constructor(rawSteamApp: RawSteamApp) : this(name = rawSteamApp.name) {
        imgUrl = IMAGE_CAPSULE_START_URL + rawSteamApp.id + IMAGE_CAPSULE_END_URL
    }

    constructor(realmSteamApp: RealmSteamApp) : this(name = realmSteamApp.name) {
        imgUrl = IMAGE_CAPSULE_START_URL + realmSteamApp.id + IMAGE_CAPSULE_END_URL
    }
}

data class SteamAppsResponse(val apps: List<RawSteamApp>) {
    fun toRealm(): List<RealmSteamApp> {
        val realmApps: MutableList<RealmSteamApp> = mutableListOf()
        apps.mapTo(realmApps, { RealmSteamApp(it) })
        return realmApps
    }
}


open class RealmDev(var name: String) : RealmObject() {
    constructor() : this("")
}

open class RealmPub(var name: String) : RealmObject() {
    constructor() : this("")
}

open class RealmTag(
        var name: String,
        var votes: Int
) : RealmObject() {
    constructor(tag: Tag) : this(tag.name, tag.votes)

    constructor() : this("", 0)
}

open class RealmSteamApp(
        @PrimaryKey
        var id: Long,
        var name: String,
        var dev: RealmList<RealmDev> = RealmList(),
        var pub: RealmList<RealmPub> = RealmList(),
        var rank: Int,
        var numOwners: Int,
        var ownersVariance: Int,
        var playersTotal: Int,
        var playersTotalVariance: Int,
        var players2Weeks: Int,
        var players2WeeksVariance: Int,
        var averageTotal: Int,
        var average2Weeks: Int,
        var medianTotal: Int,
        var median2Weeks: Int,
        var ccu: Int,
        var price: String,
        var tags: RealmList<RealmTag> = RealmList()
) : RealmObject() {
    constructor(steamApp: RawSteamApp) : this(
            id = steamApp.id,
            name = steamApp.name,
            rank = steamApp.rank,
            numOwners = steamApp.numOwners,
            ownersVariance = steamApp.ownersVariance,
            playersTotal = steamApp.playersTotal,
            playersTotalVariance = steamApp.playersTotalVariance,
            players2Weeks = steamApp.players2Weeks,
            players2WeeksVariance = steamApp.players2WeeksVariance,
            averageTotal = steamApp.averageTotal,
            average2Weeks = steamApp.average2Weeks,
            medianTotal = steamApp.medianTotal,
            median2Weeks = steamApp.median2Weeks,
            ccu = steamApp.ccu,
            price = steamApp.price
    ) {
        steamApp.dev.mapTo(dev, { RealmDev(it) })
        steamApp.pub.mapTo(pub, { RealmPub(it) })
        steamApp.tags.mapTo(tags, { RealmTag(it) })
    }

    constructor() : this(
            id = 0,
            name = "",
            rank = 0,
            numOwners = 0,
            ownersVariance = 0,
            playersTotal = 0,
            playersTotalVariance = 0,
            players2Weeks = 0,
            players2WeeksVariance = 0,
            averageTotal = 0,
            average2Weeks = 0,
            medianTotal = 0,
            median2Weeks = 0,
            ccu = 0,
            price = ""
    )
}

open class RealmAppId(var appId: Long) : RealmObject() {
    constructor() : this(0)
}

interface CustomRealmList {
    var apps: RealmList<RealmAppId>
}

open class RealmTop2Weeks(
        override var apps: RealmList<RealmAppId> = RealmList()
) : RealmObject(), CustomRealmList {
    constructor(steamApps: List<RawSteamApp>) : this() {
        steamApps.mapTo(apps, { RealmAppId(it.id) })
    }
}

open class RealmTopOwned(
        override var apps: RealmList<RealmAppId> = RealmList()
) : RealmObject(), CustomRealmList {
    constructor(steamApps: List<RawSteamApp>) : this() {
        steamApps.mapTo(apps, { RealmAppId(it.id) })
    }
}

open class RealmTopTotal(
        override var apps: RealmList<RealmAppId> = RealmList()
) : RealmObject(), CustomRealmList {
    constructor(steamApps: List<RawSteamApp>) : this() {
        steamApps.mapTo(apps, { RealmAppId(it.id) })
    }
}

open class RealmGenreAction(
        override var apps: RealmList<RealmAppId> = RealmList()
) : RealmObject(), CustomRealmList {
    constructor(steamApps: List<RawSteamApp>) : this() {
        steamApps.mapTo(apps, { RealmAppId(it.id) })
    }
}

open class RealmGenreStrategy(
        override var apps: RealmList<RealmAppId> = RealmList()
) : RealmObject(), CustomRealmList {
    constructor(steamApps: List<RawSteamApp>) : this() {
        steamApps.mapTo(apps, { RealmAppId(it.id) })
    }
}

open class RealmGenreRPG(
        override var apps: RealmList<RealmAppId> = RealmList()
) : RealmObject(), CustomRealmList {
    constructor(steamApps: List<RawSteamApp>) : this() {
        steamApps.mapTo(apps, { RealmAppId(it.id) })
    }
}

open class RealmGenreIndie(
        override var apps: RealmList<RealmAppId> = RealmList()
) : RealmObject(), CustomRealmList {
    constructor(steamApps: List<RawSteamApp>) : this() {
        steamApps.mapTo(apps, { RealmAppId(it.id) })
    }
}

open class RealmGenreAdventure(
        override var apps: RealmList<RealmAppId> = RealmList()
) : RealmObject(), CustomRealmList {
    constructor(steamApps: List<RawSteamApp>) : this() {
        steamApps.mapTo(apps, { RealmAppId(it.id) })
    }
}

open class RealmGenreSports(
        override var apps: RealmList<RealmAppId> = RealmList()
) : RealmObject(), CustomRealmList {
    constructor(steamApps: List<RawSteamApp>) : this() {
        steamApps.mapTo(apps, { RealmAppId(it.id) })
    }
}

open class RealmGenreSimulation(
        override var apps: RealmList<RealmAppId> = RealmList()
) : RealmObject(), CustomRealmList {
    constructor(steamApps: List<RawSteamApp>) : this() {
        steamApps.mapTo(apps, { RealmAppId(it.id) })
    }
}

open class RealmGenreEarlyAccess(
        override var apps: RealmList<RealmAppId> = RealmList()
) : RealmObject(), CustomRealmList {
    constructor(steamApps: List<RawSteamApp>) : this() {
        steamApps.mapTo(apps, { RealmAppId(it.id) })
    }
}

open class RealmGenreExEarlyAccess(
        override var apps: RealmList<RealmAppId> = RealmList()
) : RealmObject(), CustomRealmList {
    constructor(steamApps: List<RawSteamApp>) : this() {
        steamApps.mapTo(apps, { RealmAppId(it.id) })
    }
}

open class RealmGenreMMO(
        override var apps: RealmList<RealmAppId> = RealmList()
) : RealmObject(), CustomRealmList {
    constructor(steamApps: List<RawSteamApp>) : this() {
        steamApps.mapTo(apps, { RealmAppId(it.id) })
    }
}

open class RealmGenreFree(
        override var apps: RealmList<RealmAppId> = RealmList()
) : RealmObject(), CustomRealmList {
    constructor(steamApps: List<RawSteamApp>) : this() {
        steamApps.mapTo(apps, { RealmAppId(it.id) })
    }
}
