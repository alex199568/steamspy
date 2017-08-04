package md.ins8.steamspy.main.di

import dagger.Component
import md.ins8.steamspy.app.di.AppComponent
import md.ins8.steamspy.main.MainActivity

@MainScope
@Component(modules = arrayOf(MainModule::class), dependencies = arrayOf(AppComponent::class))
interface MainComponent {
    fun inject(activity: MainActivity)
}
