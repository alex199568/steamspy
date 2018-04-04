package md.ins8.steamspy.details

class AppDetailsPresenter(model: AppDetailsModel, private val view: AppDetailsView) {
    init {
        model.appLoadedObservable.subscribe { view.showApp(it) }
        model.loadApp()
    }
}
