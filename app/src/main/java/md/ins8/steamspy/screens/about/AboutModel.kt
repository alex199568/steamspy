package md.ins8.steamspy.screens.about

import android.content.Context
import md.ins8.steamspy.R
import md.ins8.steamspy.service.update.LAST_UPDATE_TIME_KEY

interface AboutModel {
    fun retrieveLastDataUpdateTime(): String
}

class AboutModelImpl(private val context: Context) : AboutModel {
    override fun retrieveLastDataUpdateTime(): String {
        val prefs = context.getSharedPreferences(context.getString(R.string.sharedPreferencesFileKey), Context.MODE_PRIVATE)
        val defaultMessage = context.getString(R.string.dataHasNeverBeenUpdated)
        return prefs.getString(LAST_UPDATE_TIME_KEY, defaultMessage)
    }
}
