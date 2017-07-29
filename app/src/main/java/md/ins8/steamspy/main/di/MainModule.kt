package md.ins8.steamspy.main.di

import dagger.Module
import dagger.Provides
import md.ins8.steamspy.main.MainActivity
import md.ins8.steamspy.main.mvp.MainPresenter
import md.ins8.steamspy.main.mvp.MainView


@Module
class MainModule(val activity: MainActivity) {
    @MainScope
    @Provides
    fun mainView(): MainView {
        return MainView(activity)
    }

    @MainScope
    @Provides
    fun mainPresenter(mainView: MainView): MainPresenter {
        return MainPresenter(mainView)
    }
}
