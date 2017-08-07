package md.ins8.steamspy.main.di

import dagger.Module
import dagger.Provides
import md.ins8.steamspy.main.MainActivity
import md.ins8.steamspy.main.mvp.*


@Module
class MainModule(private val activity: MainActivity) {
    @MainScope
    @Provides
    fun mainView(): MainView {
        return MainViewImpl(activity)
    }

    @MainScope
    @Provides
    fun mainPresenter(mainView: MainView, mainModel: MainModel): MainPresenter {
        return MainPresenter(mainView, mainModel)
    }

    @MainScope
    @Provides
    fun mainModel(): MainModel {
        return MainModelImpl(activity)
    }
}
