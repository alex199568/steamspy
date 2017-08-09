package md.ins8.steamspy.app_details

class AppDetailsPresenter(private val model: AppDetailsModel, private val view: AppDetailsView) {
    init {
        model.appLoadedObservable.subscribe { view.showApp(it) }
        model.loadApp()
    }
}