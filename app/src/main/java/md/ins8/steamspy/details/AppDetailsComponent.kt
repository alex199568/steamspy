package md.ins8.steamspy.details

import dagger.Component

@AppsDetailsScope
@Component(modules = arrayOf(AppDetailsModule::class))
interface AppDetailsComponent {
    fun inject(activity: AppDetailsActivity)
}