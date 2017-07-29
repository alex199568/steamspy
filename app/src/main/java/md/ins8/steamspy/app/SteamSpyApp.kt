package md.ins8.steamspy.app

import android.app.Application
import md.ins8.steamspy.app.di.AppComponent
import md.ins8.steamspy.app.di.AppModule
import md.ins8.steamspy.app.di.DaggerAppComponent
import timber.log.Timber


class SteamSpyApp : Application() {
    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())

        appComponent = DaggerAppComponent.builder().appModule(AppModule(this)).build()
    }
}