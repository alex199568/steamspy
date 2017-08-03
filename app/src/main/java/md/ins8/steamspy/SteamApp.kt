package md.ins8.steamspy

data class Tag(
        var name: String = "",
        var votes: Int = 0
)

data class RawSteamApp(
        var id: Long = -1,
        var name: String = "",
        var dev: List<String> = listOf(),
        var pub: List<String> = listOf(),
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
        var tags: List<Tag> = listOf()
)

data class SteamAppItem(
        val name: String,
        val imgUrl: String
)

data class SteamAppsResponse(val apps: List<RawSteamApp>)
