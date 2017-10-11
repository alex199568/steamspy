package md.ins8.steamspy

private const val IMAGE_CAPSULE_START_URL = "http://cdn.akamai.steamstatic.com/steam/apps/"
private const val IMAGE_CAPSULE_END_URL = "/capsule_184x69.jpg"

data class Tag(
        var name: String = "",
        var votes: Int = 0
) {
    constructor(realmTag: RealmTag) : this(realmTag.name, realmTag.votes)

    override fun toString(): String = name
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
    val imgUrl = "$IMAGE_CAPSULE_START_URL$id$IMAGE_CAPSULE_END_URL"

    val tagsText: String
        get() = tags.joinToString()

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

data class SteamAppsResponse(val apps: List<RawSteamApp>) {
    fun toRealm(): List<RealmSteamApp> {
        val realmApps: MutableList<RealmSteamApp> = mutableListOf()
        apps.mapTo(realmApps, { RealmSteamApp(it) })
        return realmApps
    }
}


