package md.ins8.steamspy.launch

interface LaunchPresenter

class LaunchPresenterImpl(private val view: LaunchView, private val model: LaunchModel) : LaunchPresenter {
    init {
        model.splashWait()

        model.eventBus.subscribe {
            when (it) {
                ModelEvent.DONE -> view.startMainActivity()
            }
        }
    }
}