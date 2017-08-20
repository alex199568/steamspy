package md.ins8.steamspy

import android.app.Application
import io.realm.Realm
import timber.log.Timber


class SteamSpyApp : Application() {
    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        Realm.init(applicationContext)

        appComponent = DaggerAppComponent.builder().appModule(AppModule(this)).build()
    }
}