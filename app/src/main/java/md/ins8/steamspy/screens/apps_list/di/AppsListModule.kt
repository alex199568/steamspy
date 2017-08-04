package md.ins8.steamspy.screens.apps_list.di

import dagger.Module
import dagger.Provides
import md.ins8.steamspy.app.di.RealmManager
import md.ins8.steamspy.screens.apps_list.AppsListFragment
import md.ins8.steamspy.screens.apps_list.AppsListType
import md.ins8.steamspy.screens.apps_list.mvp.AppsListModel
import md.ins8.steamspy.screens.apps_list.mvp.AppsListModelImpl
import md.ins8.steamspy.screens.apps_list.mvp.AppsListPresenter

@Module
class AppsListModule(private val appsListFragment: AppsListFragment, private val appsListType: AppsListType) {
    @AppsListScope
    @Provides
    fun model(realmManager: RealmManager): AppsListModel {
        return AppsListModelImpl(appsListType, realmManager)
    }

    @AppsListScope
    @Provides
    fun presenter(model: AppsListModel): AppsListPresenter {
        return AppsListPresenter(model, appsListFragment)
    }
}