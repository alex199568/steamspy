package md.ins8.steamspy.details

class AppDetailsPresenter(private val model: AppDetailsModel, private val view: AppDetailsView) {
    init {
        model.appLoadedObservable.subscribe { view.showApp(it) }
        model.loadApp()

        view.viewEvents.subscribe {
            when (it) {
                ViewEvent.CREATED -> model.viewExpanded = false
                ViewEvent.EXPAND_BTN_CLICK -> {
                    if (model.viewExpanded) {
                        model.viewExpanded = false
                        view.expanded = false
                    } else {
                        model.viewExpanded = true
                        view.expanded = true
                    }
                }
            }
        }
    }
}