package md.ins8.steamspy.launch

import dagger.Module
import dagger.Provides
import io.realm.Realm
import md.ins8.steamspy.app.di.SteamSpyAPIService

@Module
class LaunchModule(private val view: LaunchView) {
    @LaunchScope
    @Provides
    fun provideModel(steamSpyAPIService: SteamSpyAPIService, realm: Realm): LaunchModel {
        return LaunchModelImpl(steamSpyAPIService, realm)
    }

    @LaunchScope
    @Provides
    fun providePresenter(model: LaunchModel): LaunchPresenter {
        return LaunchPresenterImpl(view, model)
    }
}
