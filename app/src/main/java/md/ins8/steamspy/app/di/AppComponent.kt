package md.ins8.steamspy.app.di

import android.content.Context
import dagger.Component

@AppScope
@Component(modules = arrayOf(AppModule::class, SteamSpyAPIModule::class, DataModule::class))
interface AppComponent {
    fun context(): Context

    fun steamSpyAPIService(): SteamSpyAPIService

    fun realmManager(): RealmManager
}