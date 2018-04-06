package md.ins8.steamspy

import android.app.Application
import android.content.Context
import com.crashlytics.android.Crashlytics
import com.squareup.leakcanary.LeakCanary
import dagger.Component
import dagger.Module
import dagger.Provides
import io.fabric.sdk.android.Fabric
import io.realm.Realm
import timber.log.Timber
import javax.inject.Scope
import android.support.multidex.MultiDex

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
    fun provideContext(): Context = context
}

class SteamSpyApp : Application() {
    lateinit var appComponent: AppComponent

    override fun attachBaseContext(context: Context) {
        super.attachBaseContext(context)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return
        }
        LeakCanary.install(this)

        Timber.plant(Timber.DebugTree())
        Realm.init(applicationContext)

        appComponent = DaggerAppComponent.builder().appModule(AppModule(this)).build()

        if (!BuildConfig.DEBUG) {
            Fabric.with(this, Crashlytics())
        }
    }
}
