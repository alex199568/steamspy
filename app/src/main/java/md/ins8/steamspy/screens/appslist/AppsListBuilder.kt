package md.ins8.steamspy.screens.appslist

import android.content.Context
import dagger.Component
import dagger.Module
import dagger.Provides
import md.ins8.steamspy.AppComponent
import md.ins8.steamspy.screens.appslist.fragment.AppsListFragment
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
class AppsListModule(private val appsListFragment: AppsListFragment, private val appsListType: AppsListType) {
    @AppsListScope
    @Provides
    fun model(): AppsListModel = AppsListModelImpl(appsListType)

    @AppsListScope
    @Provides
    fun presenter(model: AppsListModel, context: Context): AppsListPresenter =
            AppsListPresenter(model, appsListFragment, context)
}