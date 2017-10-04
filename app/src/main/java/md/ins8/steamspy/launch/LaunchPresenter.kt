package md.ins8.steamspy.launch

import md.ins8.steamspy.PING_URL
import md.ins8.steamspy.pingHost


interface LaunchPresenter

class LaunchPresenterImpl(private val view: LaunchView, private val model: LaunchModel) : LaunchPresenter {
    init {
        pingHost(PING_URL, {
            if (it && model.checkFirstTime()) {
                model.updateData()
                model.setupUpdate()
            }
            model.splashWait()
        }, true)

        model.eventBus.subscribe {
            when (it) {
                ModelEvent.DONE -> view.startMainActivity()
            }
        }
    }
}