package md.ins8.steamspy.screens.about

import android.content.Context
import md.ins8.steamspy.R

class AboutPresenter(private val view: AboutView, private val model: AboutModel,
                     private val context: Context) {
    init {
        view.viewObservable.subscribe {
            val lastDataUpdate = model.retrieveLastDataUpdateTime()
            val message = context.getString(R.string.lastDataUpdate)
            view.showLastDataUpdateTime("$message $lastDataUpdate")
        }
    }
}
