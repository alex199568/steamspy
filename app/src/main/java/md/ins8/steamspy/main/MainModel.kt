package md.ins8.steamspy.main

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.support.v4.content.LocalBroadcastManager
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import md.ins8.steamspy.R
import md.ins8.steamspy.noRealmSteamApps
import md.ins8.steamspy.service.update.DataUpdateService
import md.ins8.steamspy.service.update.LOCAL_ACTION
import md.ins8.steamspy.service.update.OK_TO_START_KEY
import md.ins8.steamspy.service.update.Receiver

enum class ModelEvent {
    ALL_UPDATED
}

interface MainModel {
    val eventBus: Observable<ModelEvent>

    val appsEmpty: Boolean

    fun updateData()
}

class MainModelImpl(private val context: Context) : MainModel {
    override val eventBus: Subject<ModelEvent> = PublishSubject.create<ModelEvent>()

    override val appsEmpty: Boolean
        get() = noRealmSteamApps()

    private val receiver = Receiver()

    override fun updateData() {
        if (okToUpdate()) {
            val intent = Intent(context, DataUpdateService::class.java)
            context.startService(intent)

            val broadcastFilter = IntentFilter(LOCAL_ACTION)
            val lbm = LocalBroadcastManager.getInstance(context)
            lbm.registerReceiver(receiver, broadcastFilter)
        }
    }

    private fun okToUpdate(): Boolean {
        val prefs = context.getSharedPreferences(context.getString(R.string.sharedPreferencesFileKey), Context.MODE_PRIVATE)
        return prefs.getBoolean(OK_TO_START_KEY, true)
    }
}