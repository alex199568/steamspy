package md.ins8.steamspy

data class Tag(
        val name: String,
        val votes: Int = 0
)

data class RawSteamApp(
        val id: Long,
        val name: String = "",
        val dev: List<String> = listOf(),
        val pub: List<String> = listOf(),
        val rank: Int = 0,
        val numOwners: Int = 0,
        val playersTotal: Int = 0,
        val playersTotalVariance: Int = 0,
        val players2Weeks: Int = 0,
        val players2WeeksVariance: Int = 0,
        val averageTotal: Int = 0,
        val average2Weeks: Int = 0,
        val medianTotal: Int = 0,
        val median2Weeks: Int = 0,
        val ccu: Int = 0,
        val price: String = "0",
        val tags: List<Tag> = listOf()
)

data class MinSteamApp(
        val name: String,
        val imgUrl: String,
        val tags: List<Tag> = listOf()
)
