package md.ins8.steamspy.main.mvp

import android.content.Context
import android.content.Intent
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import md.ins8.steamspy.R
import md.ins8.steamspy.update_service.DataUpdateService

enum class ModelEvent(val messageRes: Int) {
    ALL_DOWNLOADED(R.string.downloadingAll),
    TOP_2_WEEKS_DOWNLOADED(R.string.downloadingTop2Weeks),
    TOP_OWNED_DOWNLOADED(R.string.downloadingTopOwned),
    TOP_TOTAL_DOWNLOADED(R.string.downloadingTopTotal),
    GENRE_ACTION_DOWNLOADED(R.string.downloadingGenreAction),
    GENRE_STRATEGY_DOWNLOADED(R.string.downloadingGenreStrategy),
    GENRE_RPG_DOWNLOADED(R.string.downloadingGenreRPG),
    GENRE_INDIE_DOWNLOADED(R.string.downloadingGenreIndie),
    GENRE_ADVENTURE_DOWNLOADED(R.string.downloadingGenreAdventure),
    GENRE_SPORTS_DOWNLOADED(R.string.downloadingGenreSports),
    GENRE_SIMULATION_DOWNLOADED(R.string.downloadingGenreSimulation),
    GENRE_EARLY_ACCESS_DOWNLOADED(R.string.downloadingGenreEarlyAccess),
    GENRE_EX_EARLY_ACCESS_DOWNLOADED(R.string.downloadingGenreExEarlyAccess),
    GENRE_MMO_DOWNLOADED(R.string.downloadingGenreMmo),
    GENRE_FREE_DOWNLOADED(R.string.downloadingGenreFree),

    ALL_UPDATED(R.string.updatingAll),
    TOP_2_WEEKS_UPDATED(R.string.updatingTop2Weeks),
    TOP_OWNED_UPDATED(R.string.updatingTopOwned),
    TOP_TOTAL_UPDATED(R.string.updatingTopTotal),
    GENRE_ACTION_UPDATED(R.string.updatingGenreAction),
    GENRE_STRATEGY_UPDATED(R.string.updatingGenreStrategy),
    GENRE_RPG_UPDATED(R.string.updatingGenreRPG),
    GENRE_INDIE_UPDATED(R.string.updatingGenreIndie),
    GENRE_ADVENTURE_UPDATED(R.string.updatingGenreAdventure),
    GENRE_SPORTS_UPDATED(R.string.updatingGenreSports),
    GENRE_SIMULATION_UPDATED(R.string.updatingGenreSimulation),
    GENRE_EARLY_ACCESS_UPDATED(R.string.updatingGenreEarlyAccess),
    GENRE_EX_EARLY_ACCESS_UPDATED(R.string.updatingGenreExEarlyAccess),
    GENRE_MMO_UPDATED(R.string.updatingGenreMmo),
    GENRE_FREE_UPDATED(R.string.updatingGenreFree),

}

interface MainModel {
    val eventBus: Observable<ModelEvent>

    fun updateData()
}

class MainModelImpl(private val context: Context) : MainModel {
    override val eventBus: Subject<ModelEvent> = PublishSubject.create<ModelEvent>()

    override fun updateData() {
        val intent = Intent(context, DataUpdateService::class.java)
        context.startService(intent)
    }
}