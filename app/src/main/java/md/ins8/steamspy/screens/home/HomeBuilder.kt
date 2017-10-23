package md.ins8.steamspy.screens.home

import dagger.Component
import dagger.Module
import dagger.Provides
import javax.inject.Scope

@Scope
@Retention annotation class HomeScope

@Module
class HomeModule(private val fragment: HomeFragment) {
    @HomeScope
    @Provides
    fun provideView(): HomeView = fragment

    @HomeScope
    @Provides
    fun provideModel(): HomeModel = HomeModelImpl()

    @HomeScope
    @Provides
    fun providePresenter(view: HomeView, model: HomeModel) = HomePresenter(view, model)
}

@HomeScope
@Component(modules = arrayOf(HomeModule::class))
interface HomeComponent {
    fun inject(fragment: HomeFragment)
}
