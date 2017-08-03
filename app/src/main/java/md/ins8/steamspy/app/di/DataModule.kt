package md.ins8.steamspy.app.di

import android.content.Context
import dagger.Module
import dagger.Provides
import io.realm.Realm

@Module
class DataModule {
    @AppScope
    @Provides
    fun provideRealm(context: Context): Realm {
        Realm.init(context)
        Realm.deleteRealm(Realm.getDefaultConfiguration())
        return Realm.getDefaultInstance()
    }
}
