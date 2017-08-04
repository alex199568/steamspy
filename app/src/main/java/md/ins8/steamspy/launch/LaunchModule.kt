package md.ins8.steamspy.launch

import dagger.Module
import dagger.Provides
import md.ins8.steamspy.app.di.SteamSpyAPIService

@Module
class LaunchModule(private val view: LaunchView) {
    @LaunchScope
    @Provides
    fun provideModel(steamSpyAPIService: SteamSpyAPIService): LaunchModel {
        return LaunchModelImpl(steamSpyAPIService)
    }

    @LaunchScope
    @Provides
    fun providePresenter(model: LaunchModel): LaunchPresenter {
        return LaunchPresenterImpl(view, model)
    }
}
