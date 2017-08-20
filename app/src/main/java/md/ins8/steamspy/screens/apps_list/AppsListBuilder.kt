package md.ins8.steamspy.screens.apps_list

import dagger.Component
import dagger.Module
import dagger.Provides
import md.ins8.steamspy.AppComponent
import md.ins8.steamspy.screens.apps_list.fragment.AppsListFragment
import md.ins8.steamspy.screens.apps_list.mvp.AppsListModel
import md.ins8.steamspy.screens.apps_list.mvp.AppsListModelImpl
import md.ins8.steamspy.screens.apps_list.mvp.AppsListPresenter
import javax.inject.Scope

@Scope
@Retention
annotation class AppsListScope


@AppsListScope
@Component(modules = arrayOf(AppsListModule::class), dependencies = arrayOf(AppComponent::class))
interface AppsListComponent {
    fun inject(appsListFragment: AppsListFragment)

    fun presenter(): AppsListPresenter
}


@Module
class AppsListModule(private val appsListFragment: AppsListFragment, private val appsListType: AppsListType) {
    @AppsListScope
    @Provides
    fun model(): AppsListModel = AppsListModelImpl(appsListType)

    @AppsListScope
    @Provides
    fun presenter(model: AppsListModel): AppsListPresenter =
            AppsListPresenter(model, appsListFragment)
}