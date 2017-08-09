package md.ins8.steamspy.app_details

import dagger.Module
import dagger.Provides

@Module
class AppDetailsModule(private val view: AppDetailsView, private val appId: Long) {
    @AppsDetailsScope
    @Provides
    fun provideModel(): AppDetailsModel {
        return AppDetailsModelImpl(appId)
    }

    @AppsDetailsScope
    @Provides
    fun providePresenter(model: AppDetailsModel): AppDetailsPresenter {
        return AppDetailsPresenter(model, view)
    }
}