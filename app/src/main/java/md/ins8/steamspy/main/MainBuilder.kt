package md.ins8.steamspy.main

import dagger.Component
import dagger.Module
import dagger.Provides
import md.ins8.steamspy.AppComponent
import javax.inject.Scope

@Scope
@Retention
annotation class MainScope


@MainScope
@Component(modules = arrayOf(MainModule::class), dependencies = arrayOf(AppComponent::class))
interface MainComponent {
    fun inject(activity: MainActivity)
}

@Module
class MainModule(private val activity: MainActivity) {
    @MainScope
    @Provides
    fun mainView(): MainView = activity

    @MainScope
    @Provides
    fun mainPresenter(mainView: MainView, mainModel: MainModel): MainPresenter =
            MainPresenter(mainView, mainModel)

    @MainScope
    @Provides
    fun mainModel(): MainModel = MainModelImpl(activity)
}
