package md.ins8.steamspy.screens.apps_list.di

import dagger.Component
import md.ins8.steamspy.app.di.AppComponent
import md.ins8.steamspy.screens.apps_list.AppsListFragment

@AppsListScope
@Component(modules = arrayOf(AppsListModule::class), dependencies = arrayOf(AppComponent::class))
interface AppsListComponent {
    fun inject(appsListFragment: AppsListFragment)
}
