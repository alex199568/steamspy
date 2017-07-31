package md.ins8.steamspy.screens.apps_list.mvp

import md.ins8.steamspy.screens.apps_list.AppsListFragment
import md.ins8.steamspy.screens.apps_list.AppsListType


class AppsListPresenter(private val model: AppsListModel, private val view: AppsListFragment) {
    fun start(appsListType: AppsListType) {
        val data = model.fetchData(appsListType)
        view.viewEventBus.subscribe {
            view.setData(data)
        }
    }
}