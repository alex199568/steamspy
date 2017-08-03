package md.ins8.steamspy.launch

import dagger.Component
import md.ins8.steamspy.app.di.AppComponent

@LaunchScope
@Component(modules = arrayOf(LaunchModule::class), dependencies = arrayOf(AppComponent::class))
interface LaunchComponent {
    fun inject(launchActivity: LaunchActivity)
}
