package md.ins8.steamspy.app.di

import android.content.Context
import dagger.Module
import dagger.Provides
import io.realm.Realm

class RealmManager(context: Context) {
    private var realm: Realm? = null

    init {
        Realm.init(context)
    }

    fun create(): Realm {
        realm = Realm.getDefaultInstance()
        return realm!!
    }

    fun close() {
        realm?.close()
    }

    fun delete() {
        Realm.deleteRealm(Realm.getDefaultConfiguration())
    }
}

@Module
class DataModule {
    @AppScope
    @Provides
    fun provideRealmManager(context: Context): RealmManager {
        return RealmManager(context)
    }
}
