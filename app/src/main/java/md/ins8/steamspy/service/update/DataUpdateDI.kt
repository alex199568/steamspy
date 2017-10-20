package md.ins8.steamspy.service.update

import android.content.Context
import dagger.Component
import dagger.Module
import dagger.Provides
import md.ins8.steamspy.AppComponent
import javax.inject.Scope


@Scope
@Retention
annotation class DataUpdateScope

@Component(dependencies = arrayOf(AppComponent::class), modules = arrayOf(DataUpdateServiceModule::class))
@DataUpdateScope
interface DataUpdateComponent {
    fun inject(service: DataUpdateService)
}

@Module
class DataUpdateServiceModule {
    @DataUpdateScope
    @Provides
    fun provideAutoUpdateChecker(context: Context): AutoUpdateConditionsChecker =
            AutoUpdateConditionsChecker(context)
}