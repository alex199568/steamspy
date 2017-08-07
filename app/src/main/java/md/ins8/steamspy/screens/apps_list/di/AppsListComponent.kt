package md.ins8.steamspy.screens.apps_list.di

import dagger.Component
import md.ins8.steamspy.app.di.AppComponent
import md.ins8.steamspy.screens.apps_list.AppsListFragment
import md.ins8.steamspy.screens.apps_list.mvp.AppsListPresenter

@AppsListScope
@Component(modules = arrayOf(AppsListModule::class), dependencies = arrayOf(AppComponent::class))
interface AppsListComponent {
    fun inject(appsListFragment: AppsListFragment)

    fun presenter(): AppsListPresenter
}
