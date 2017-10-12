package md.ins8.steamspy.screens.about

import dagger.Component
import dagger.Module
import dagger.Provides
import javax.inject.Scope

@Scope
@Retention annotation class AboutScope

@AboutScope
@Component(modules = arrayOf(AboutModule::class))
interface AboutComponent {
    fun inject(aboutFragment: AboutFragment)
    fun presenter(): AboutPresenter
}

@Module
class AboutModule(private val aboutFragment: AboutFragment) {
    @AboutScope
    @Provides
    fun provideModel(): AboutModel =
            AboutModelImpl(aboutFragment.context)

    @AboutScope
    @Provides
    fun providePresenter(aboutModel: AboutModel): AboutPresenter =
            AboutPresenter(aboutFragment, aboutModel, aboutFragment.context)
}

