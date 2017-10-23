package md.ins8.steamspy.screens.home

class HomePresenter(private val view: HomeView, private val model: HomeModel) {
    init {
        view.eventsObservable.subscribe {
            when (it) {
                HomeViewEvent.VIEW_CREATED -> {
                    val appsCount = model.numberOfApps
                    view.showAppsCount(appsCount)
                }
            }
        }
    }
}
