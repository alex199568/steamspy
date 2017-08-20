package md.ins8.steamspy

import android.content.Context
import dagger.Component
import dagger.Module
import dagger.Provides
import javax.inject.Scope

@Scope
@Retention annotation class AppScope


@AppScope
@Component(modules = arrayOf(AppModule::class, SteamSpyAPIModule::class))
interface AppComponent {
    fun context(): Context

    fun steamSpyAPIService(): SteamSpyAPIService
}


@Module
class AppModule(val context: Context) {
    @AppScope
    @Provides
    fun provideContext(): Context {
        return context
    }
}
