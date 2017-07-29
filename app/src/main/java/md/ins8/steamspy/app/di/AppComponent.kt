package md.ins8.steamspy.app.di

import android.content.Context
import dagger.Component

@AppScope
@Component(modules = arrayOf(AppModule::class))
interface AppComponent {
    fun context(): Context
}