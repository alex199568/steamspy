package md.ins8.steamspy

data class ListType(
        val id: Int,
        val displayName: Int,
        val paramName: String
)

enum class ListTypes(val listType: ListType) {
    ALL(ListType(0, R.string.titleAll, "all"))
}

enum class TopListTypes(val listType: ListType) {
    TOP_2_WEEKS(ListType(1, R.string.titleTop2Weeks, "top100in2weeks")),
    TOP_OWNED(ListType(2, R.string.titleTopOwned, "top100owned")),
    TOP_TOTAL(ListType(3, R.string.titleTopTotal, "top100forever")),
}

enum class GenreListTypes(val listType: ListType) {
    GENRE_ACTION(ListType(4, R.string.titleGenreAction, "Action")),
    GENRE_STRATEGY(ListType(5, R.string.titleGenreStrategy, "Strategy")),
    GENRE_RPG(ListType(6, R.string.titleGenreRPG, "RPG")),
    GENRE_INDIE(ListType(7, R.string.titleGenreIndie, "Indie")),
    GENRE_ADVENTURE(ListType(8, R.string.titleGenreAdventure, "Adventure")),
    GENRE_SPORTS(ListType(9, R.string.titleGenreSports, "Sports")),
    GENRE_SIMULATION(ListType(10, R.string.titleGenreSimulation, "Simulation")),
    GENRE_EARLY_ACCESS(ListType(11, R.string.titleGenreEarlyAccess, "Early+Access")),
    GENRE_EX_EARLY_ACCESS(ListType(12, R.string.titleGenreExEarlyAccess, "Ex+Early+Access")),
    GENRE_MMO(ListType(13, R.string.titleGenreMMO, "Massively+Multiplayer")),
    GENRE_FREE(ListType(14, R.string.titleGenreFree, "Free"))
}

