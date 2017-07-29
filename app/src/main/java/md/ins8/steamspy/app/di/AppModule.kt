package md.ins8.steamspy.app.di

import android.content.Context
import dagger.Module
import dagger.Provides

@Module
class AppModule(val context: Context) {
    @AppScope
    @Provides
    fun provideContext(): Context {
        return context
    }
}
