package md.ins8.steamspy.launch

import dagger.Module
import dagger.Provides

@Module
class LaunchModule(private val view: LaunchView) {
    @LaunchScope
    @Provides
    fun provideModel(): LaunchModel {
        return LaunchModelImpl()
    }

    @LaunchScope
    @Provides
    fun providePresenter(model: LaunchModel): LaunchPresenter {
        return LaunchPresenterImpl(view, model)
    }
}
