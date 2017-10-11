package md.ins8.steamspy.screens.appslist

import dagger.Component
import dagger.Module
import dagger.Provides
import md.ins8.steamspy.AppComponent
import md.ins8.steamspy.screens.appslist.mvp.AppsListFragment
import md.ins8.steamspy.screens.appslist.mvp.AppsListModel
import md.ins8.steamspy.screens.appslist.mvp.AppsListModelImpl
import md.ins8.steamspy.screens.appslist.mvp.AppsListPresenter
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
class AppsListModule(private val appsListFragment: AppsListFragment,
                     private val listType: Int, private val listTypeId: Int,
                     private val searchParam: String = "") {
    @AppsListScope
    @Provides
    fun model(): AppsListModel = AppsListModelImpl()

    @AppsListScope
    @Provides
    fun presenter(model: AppsListModel): AppsListPresenter =
            AppsListPresenter(model, appsListFragment, appsListFragment.context, listType, listTypeId, searchParam)
}