package md.ins8.steamspy.screens.settings

import android.content.Context
import dagger.Component
import dagger.Module
import dagger.Provides
import md.ins8.steamspy.AbstractAlarmManager
import md.ins8.steamspy.AppComponent
import md.ins8.steamspy.DataUpdateAlarmManager
import javax.inject.Scope

@Scope
@Retention
annotation class SettingsScope

@Module
class SettingsModule {
    @SettingsScope
    @Provides
    fun provideAbstractAlarmManager(context: Context): AbstractAlarmManager =
            DataUpdateAlarmManager(context)
}

@SettingsScope
@Component(modules = arrayOf(SettingsModule::class), dependencies = arrayOf(AppComponent::class))
interface SettingsComponent {
    fun inject(settingsFragment: SettingsFragment)
}
