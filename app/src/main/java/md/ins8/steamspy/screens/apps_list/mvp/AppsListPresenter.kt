package md.ins8.steamspy.screens.apps_list.mvp

import md.ins8.steamspy.app_details.startAppDetailsActivity
import md.ins8.steamspy.screens.apps_list.AppsListView
import md.ins8.steamspy.screens.apps_list.AppsListViewEvent


class AppsListPresenter(private val model: AppsListModel, private val view: AppsListView) {
    init {
        view.eventBus.subscribe {
            when (it) {
                AppsListViewEvent.VIEW_CREATED -> onViewCreated()
            }
        }

        view.itemClickObservable.subscribe { startAppDetailsActivity(view.appsListContext, it) }
    }

    private fun onViewCreated() {
        model.fetchAppsList()
        model.appsObservable.subscribe {
            view.showAppsList(it)
        }
    }
}
