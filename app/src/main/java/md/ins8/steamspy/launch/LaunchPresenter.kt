package md.ins8.steamspy.launch


interface LaunchPresenter

class LaunchPresenterImpl(private val view: LaunchView, private val model: LaunchModel) : LaunchPresenter {
    init {
        if (model.checkFirstTime()) {
            model.updateData()
            model.setupUpdate()
        } else {
            model.splashWait()
        }

        model.eventBus.subscribe {
            when (it) {
                ModelEvent.DONE -> view.startMainActivity()
                ModelEvent.DATA_UPDATED -> view.startMainActivity()
            }
        }
    }
}