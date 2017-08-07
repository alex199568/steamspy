package md.ins8.steamspy.app.di

import dagger.Module
import dagger.Provides
import io.realm.Realm

class RealmManager {
    private var realm: Realm? = null


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
    fun provideRealmManager(): RealmManager {
        return RealmManager()
    }
}
