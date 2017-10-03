package md.ins8.steamspy.service.update

import dagger.Component
import md.ins8.steamspy.AppComponent
import javax.inject.Scope


@Scope
@Retention
annotation class DataUpdateScope

@Component(dependencies = arrayOf(AppComponent::class))
@DataUpdateScope
interface DataUpdateComponent {
    fun inject(service: DataUpdateService)
}