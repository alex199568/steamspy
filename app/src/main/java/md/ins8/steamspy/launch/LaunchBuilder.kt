package md.ins8.steamspy.launch

import dagger.Component
import dagger.Module
import dagger.Provides
import md.ins8.steamspy.AppComponent
import javax.inject.Scope

@Scope
@Retention
annotation class LaunchScope


@LaunchScope
@Component(modules = arrayOf(LaunchModule::class), dependencies = arrayOf(AppComponent::class))
interface LaunchComponent {
    fun inject(launchActivity: LaunchActivity)
}

@Module
class LaunchModule(private val view: LaunchView) {
    @LaunchScope
    @Provides
    fun provideModel(): LaunchModel = LaunchModelImpl()

    @LaunchScope
    @Provides
    fun providePresenter(model: LaunchModel): LaunchPresenter = LaunchPresenterImpl(view, model)
}
