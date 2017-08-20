package md.ins8.steamspy.update_service

import dagger.Component
import md.ins8.steamspy.AppComponent

@Component(dependencies = arrayOf(AppComponent::class))
@DataUpdateScope
interface DataUpdateComponent {
    fun inject(service: DataUpdateService)
}