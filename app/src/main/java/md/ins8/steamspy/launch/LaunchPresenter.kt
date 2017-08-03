package md.ins8.steamspy.launch


interface LaunchPresenter

class LaunchPresenterImpl(private val view: LaunchView, private val model: LaunchModel) : LaunchPresenter {
    init {
        model.eventBus.subscribe {
            view.startMainActivity()
        }
    }
}