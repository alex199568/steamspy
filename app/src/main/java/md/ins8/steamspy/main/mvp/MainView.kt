package md.ins8.steamspy.main.mvp

import android.content.Context
import android.support.v4.app.Fragment
import android.view.Menu
import android.view.MenuItem
import co.zsmb.materialdrawerkt.builders.drawer
import co.zsmb.materialdrawerkt.draweritems.badgeable.primaryItem
import co.zsmb.materialdrawerkt.draweritems.badgeable.secondaryItem
import co.zsmb.materialdrawerkt.draweritems.divider
import co.zsmb.materialdrawerkt.draweritems.expandable.expandableItem
import com.afollestad.materialcab.MaterialCab
import io.reactivex.subjects.BehaviorSubject
import md.ins8.steamspy.R
import md.ins8.steamspy.main.MainActivity
import md.ins8.steamspy.main.NavigationEvent
import md.ins8.steamspy.screens.about.AboutFragment
import md.ins8.steamspy.screens.apps_list.AppsListType
import md.ins8.steamspy.screens.apps_list.newAppsListFragmentInstance
import md.ins8.steamspy.screens.home.HomeFragment
import md.ins8.steamspy.screens.notifications.NotificationsFragment
import md.ins8.steamspy.screens.settings.SettingsFragment


class MainView(val activity: MainActivity) {
    val navigationEventBus = BehaviorSubject.create<NavigationEvent>()

    private val context: Context

    init {
        context = activity

        val materialCab = MaterialCab(activity, R.id.cab_stub).start(object : MaterialCab.Callback {
            override fun onCabFinished(cab: MaterialCab?): Boolean {
                return false
            }

            override fun onCabItemClicked(item: MenuItem?): Boolean {
                return false
            }

            override fun onCabCreated(cab: MaterialCab?, menu: Menu?): Boolean {
                cab?.setTitle("Home")
                return true
            }
        })

        activity.drawer {
            headerViewRes = R.layout.drawer_header
            toolbar = materialCab.toolbar
            primaryItem(R.string.navigation_home) {
                onClick { _ -> navigationEventBus.onNext(NavigationEvent.HOME); false }
                icon = R.drawable.ic_home_black_24dp
            }
            primaryItem(R.string.navigation_all) {
                onClick { _ -> navigationEventBus.onNext(NavigationEvent.ALL); false }
                icon = R.drawable.ic_view_list_black_24dp
            }
            divider { }
            expandableItem(R.string.navigation_top) {
                secondaryItem(context.getString(R.string.navTop2Weeks)) {
                    onClick { _ -> navigationEventBus.onNext(NavigationEvent.TOP_2_WEEKS); false }
                    icon = R.drawable.ic_top_2w
                }
                secondaryItem(context.getString(R.string.navTopOwned)) {
                    onClick { _ -> navigationEventBus.onNext(NavigationEvent.TOP_OWNED); false }
                    icon = R.drawable.ic_ow
                }
                secondaryItem(context.getString(R.string.navTopTotal)) {
                    onClick { _ -> navigationEventBus.onNext(NavigationEvent.TOP_TOTAL); false }
                    icon = R.drawable.ic_tl
                }
                selectable = false
                icon = R.drawable.ic_call_made_black_24dp
            }
            divider { }
            expandableItem(R.string.navigation_genre) {
                secondaryItem(context.getString(R.string.navGenreAction)) {
                    icon = R.drawable.ic_ac
                    onClick { _ -> navigationEventBus.onNext(NavigationEvent.GENRE_ACTION); false }
                }
                secondaryItem(context.getString(R.string.navGenreStrategy)) {
                    icon = R.drawable.ic_st
                    onClick { _ -> navigationEventBus.onNext(NavigationEvent.GENRE_STRATEGY); false }
                }
                secondaryItem(context.getString(R.string.navGenreRPG)) {
                    icon = R.drawable.ic_rp
                    onClick { _ -> navigationEventBus.onNext(NavigationEvent.GENRE_RPG); false }
                }
                secondaryItem(context.getString(R.string.navGenreIndie)) {
                    icon = R.drawable.ic_in
                    onClick { _ -> navigationEventBus.onNext(NavigationEvent.GENRE_INDIE); false }
                }
                secondaryItem(context.getString(R.string.navGenreAdventure)) {
                    icon = R.drawable.ic_ad
                    onClick { _ -> navigationEventBus.onNext(NavigationEvent.GENRE_ADVENTURE); false }
                }
                secondaryItem(context.getString(R.string.navGenreSports)) {
                    icon = R.drawable.ic_sp
                    onClick { _ -> navigationEventBus.onNext(NavigationEvent.GENRE_SPORTS); false }
                }
                secondaryItem(context.getString(R.string.navGenreSimulation)) {
                    icon = R.drawable.ic_sm
                    onClick { _ -> navigationEventBus.onNext(NavigationEvent.GENRE_SIMULATION); false }
                }
                secondaryItem(context.getString(R.string.navGenreEarlyAccess)) {
                    icon = R.drawable.ic_ea
                    onClick { _ -> navigationEventBus.onNext(NavigationEvent.GENRE_EARLY_ACCESS); false }
                }
                secondaryItem(context.getString(R.string.navGenreExEarlyAccess)) {
                    icon = R.drawable.ic_ee
                    onClick { _ -> navigationEventBus.onNext(NavigationEvent.GENRE_EX_EARLY_ACCESS); false }
                }
                secondaryItem(context.getString(R.string.navGenreMMO)) {
                    icon = R.drawable.ic_mm
                    onClick { _ -> navigationEventBus.onNext(NavigationEvent.GENRE_MMO); false }
                }
                secondaryItem(context.getString(R.string.navGenreFree)) {
                    icon = R.drawable.ic_fr
                    onClick { _ -> navigationEventBus.onNext(NavigationEvent.GENRE_FREE); false }
                }
                selectable = false
                icon = R.drawable.ic_star_black_24dp
            }
            divider { }
            primaryItem(R.string.navigation_notifications) {
                icon = R.drawable.ic_notifications_black_24dp
                onClick { _ -> navigationEventBus.onNext(NavigationEvent.NOTIFICATIONS); false }
            }
            primaryItem(R.string.navigation_settings) {
                icon = R.drawable.ic_settings_black_24dp
                onClick { _ -> navigationEventBus.onNext(NavigationEvent.SETTINGS); false }
            }
            primaryItem(R.string.navigation_about) {
                icon = R.drawable.ic_info_black_24dp
                onClick { _ -> navigationEventBus.onNext(NavigationEvent.ABOUT); false }
            }
        }
    }

    fun switchToHomeFragment() {
        val homeFragment = HomeFragment()

        replaceFragment(homeFragment)
    }

    fun switchToAboutFragment() {
        val aboutFragment = AboutFragment()

        replaceFragment(aboutFragment)
    }

    fun switchToNotificationsFragment() {
        val notificationsFragment = NotificationsFragment()

        replaceFragment(notificationsFragment)
    }

    fun switchToSettingsFragment() {
        val settingsFragment = SettingsFragment()

        replaceFragment(settingsFragment)
    }

    fun switchToAppsListFragment(appsListType: AppsListType) {
        val appsListFragment = newAppsListFragmentInstance(appsListType)

        replaceFragment(appsListFragment)
    }

    private fun replaceFragment(fragment: Fragment) {
        val transaction = activity.supportFragmentManager.beginTransaction()
        transaction.replace(R.id.mainContainer, fragment)
        transaction.commit()
    }
}
