package md.ins8.steamspy.main

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.InputType
import android.view.Menu
import android.view.MenuItem
import co.zsmb.materialdrawerkt.builders.accountHeader
import co.zsmb.materialdrawerkt.builders.drawer
import co.zsmb.materialdrawerkt.draweritems.badgeable.primaryItem
import co.zsmb.materialdrawerkt.draweritems.badgeable.secondaryItem
import co.zsmb.materialdrawerkt.draweritems.divider
import co.zsmb.materialdrawerkt.draweritems.expandable.expandableItem
import com.afollestad.materialcab.MaterialCab
import com.afollestad.materialdialogs.MaterialDialog
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import md.ins8.steamspy.*
import md.ins8.steamspy.screens.about.AboutFragment
import md.ins8.steamspy.screens.appslist.AppsListFragment
import md.ins8.steamspy.screens.appslist.newAppsListFragmentInstance
import md.ins8.steamspy.screens.empty.home.EmptyHomeFragment
import md.ins8.steamspy.screens.home.HomeFragment
import md.ins8.steamspy.screens.settings.SettingsFragment
import javax.inject.Inject

enum class ViewEvent {
    ACTION_UPDATE_DATA,
    ACTION_SEARCH,
    UPDATE_CONFIRMED,
    RESUME
}

enum class NavigationEvent(val titleStrRes: Int) {
    HOME(R.string.titleHome),
    ALL(R.string.titleAll),
    TOP_2_WEEKS(R.string.titleTop2Weeks),
    TOP_OWNED(R.string.titleTopOwned),
    TOP_TOTAL(R.string.titleTopTotal),
    GENRE_ACTION(R.string.titleGenreAction),
    GENRE_STRATEGY(R.string.titleGenreStrategy),
    GENRE_RPG(R.string.titleGenreRPG),
    GENRE_INDIE(R.string.titleGenreIndie),
    GENRE_ADVENTURE(R.string.titleGenreAdventure),
    GENRE_SPORTS(R.string.titleGenreSports),
    GENRE_SIMULATION(R.string.titleGenreSimulation),
    GENRE_EARLY_ACCESS(R.string.titleGenreEarlyAccess),
    GENRE_EX_EARLY_ACCESS(R.string.titleGenreExEarlyAccess),
    GENRE_MMO(R.string.titleGenreMMO),
    GENRE_FREE(R.string.titleGenreFree),
    SETTINGS(R.string.navSettings),
    ABOUT(R.string.titleAbout)
}


interface MainView {
    val navigationEventBus: Observable<NavigationEvent>
    val eventBus: Observable<ViewEvent>
    val inputEvents: Observable<String>

    fun switchToHomeFragment()
    fun switchToEmptyHomeFragment()
    fun switchToAboutFragment()
    fun switchToSettingsFragment()
    fun switchToAppsListFragment(listType: ListType)
    fun switchToAppsListFragment(searchFor: String)

    fun refreshListFragment()
    fun showInputDialog()
    fun confirmUpdate()
}


class MainActivity : BaseActivity(), MainView {
    @Inject
    lateinit var presenter: MainPresenter

    private var materialCab: MaterialCab? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        materialCab = MaterialCab(this, R.id.cab_stub)
                .setMenu(R.menu.main_menu)
                .start(object : MaterialCab.Callback {
                    override fun onCabFinished(cab: MaterialCab?): Boolean = false

                    override fun onCabItemClicked(item: MenuItem?): Boolean {
                        when (item?.itemId) {
                            R.id.actionUpdateData -> eventBus.onNext(ViewEvent.ACTION_UPDATE_DATA)
                            R.id.actionSearch -> eventBus.onNext(ViewEvent.ACTION_SEARCH)
                        }
                        return true
                    }

                    override fun onCabCreated(cab: MaterialCab?, menu: Menu?): Boolean {
                        listOf(
                                R.id.actionUpdateData, R.id.actionSearch
                        ).forEach {
                            menu?.findItem(it)?.icon?.changeIconColor(Color.WHITE)
                        }

                        return true
                    }
                })

        drawer {
            accountHeader { background = R.drawable.fractal }
            toolbar = materialCab?.toolbar!!
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
                secondaryItem(getString(R.string.navTop2Weeks)) {
                    onClick { _ -> navigationEventBus.onNext(NavigationEvent.TOP_2_WEEKS); false }
                    icon = R.drawable.ic_top_2w
                }
                secondaryItem(getString(R.string.navTopOwned)) {
                    onClick { _ -> navigationEventBus.onNext(NavigationEvent.TOP_OWNED); false }
                    icon = R.drawable.ic_ow
                }
                secondaryItem(getString(R.string.navTopTotal)) {
                    onClick { _ -> navigationEventBus.onNext(NavigationEvent.TOP_TOTAL); false }
                    icon = R.drawable.ic_tl
                }
                selectable = false
                icon = R.drawable.ic_call_made_black_24dp
            }
            divider { }
            expandableItem(R.string.navigation_genre) {
                secondaryItem(getString(R.string.navGenreAction)) {
                    icon = R.drawable.ic_ac
                    onClick { _ -> navigationEventBus.onNext(NavigationEvent.GENRE_ACTION); false }
                }
                secondaryItem(getString(R.string.navGenreStrategy)) {
                    icon = R.drawable.ic_st
                    onClick { _ -> navigationEventBus.onNext(NavigationEvent.GENRE_STRATEGY); false }
                }
                secondaryItem(getString(R.string.navGenreRPG)) {
                    icon = R.drawable.ic_rp
                    onClick { _ -> navigationEventBus.onNext(NavigationEvent.GENRE_RPG); false }
                }
                secondaryItem(getString(R.string.navGenreIndie)) {
                    icon = R.drawable.ic_in
                    onClick { _ -> navigationEventBus.onNext(NavigationEvent.GENRE_INDIE); false }
                }
                secondaryItem(getString(R.string.navGenreAdventure)) {
                    icon = R.drawable.ic_ad
                    onClick { _ -> navigationEventBus.onNext(NavigationEvent.GENRE_ADVENTURE); false }
                }
                secondaryItem(getString(R.string.navGenreSports)) {
                    icon = R.drawable.ic_sp
                    onClick { _ -> navigationEventBus.onNext(NavigationEvent.GENRE_SPORTS); false }
                }
                secondaryItem(getString(R.string.navGenreSimulation)) {
                    icon = R.drawable.ic_sm
                    onClick { _ -> navigationEventBus.onNext(NavigationEvent.GENRE_SIMULATION); false }
                }
                secondaryItem(getString(R.string.navGenreEarlyAccess)) {
                    icon = R.drawable.ic_ea
                    onClick { _ -> navigationEventBus.onNext(NavigationEvent.GENRE_EARLY_ACCESS); false }
                }
                secondaryItem(getString(R.string.navGenreExEarlyAccess)) {
                    icon = R.drawable.ic_ee
                    onClick { _ -> navigationEventBus.onNext(NavigationEvent.GENRE_EX_EARLY_ACCESS); false }
                }
                secondaryItem(getString(R.string.navGenreMMO)) {
                    icon = R.drawable.ic_mm
                    onClick { _ -> navigationEventBus.onNext(NavigationEvent.GENRE_MMO); false }
                }
                secondaryItem(getString(R.string.navGenreFree)) {
                    icon = R.drawable.ic_fr
                    onClick { _ -> navigationEventBus.onNext(NavigationEvent.GENRE_FREE); false }
                }
                selectable = false
                icon = R.drawable.ic_star_black_24dp
            }
            divider { }
            primaryItem(R.string.navSettings) {
                icon = R.drawable.ic_settings_black_24dp
                onClick { _ -> navigationEventBus.onNext(NavigationEvent.SETTINGS); false }
            }
            primaryItem(R.string.navigation_about) {
                icon = R.drawable.ic_info_black_24dp
                onClick { _ -> navigationEventBus.onNext(NavigationEvent.ABOUT); false }
            }
        }

        DaggerMainComponent.builder()
                .appComponent((application as SteamSpyApp).appComponent)
                .mainModule(MainModule(this)).build().inject(this)
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        presenter.safeToUpdateUI = true
        eventBus.onNext(ViewEvent.RESUME)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        presenter.safeToUpdateUI = false
    }

    override val navigationEventBus: Subject<NavigationEvent> = BehaviorSubject.create<NavigationEvent>()
    override val eventBus: Subject<ViewEvent> = BehaviorSubject.create<ViewEvent>()
    override val inputEvents: Subject<String> = PublishSubject.create<String>()

    private var lastFragment: Fragment? = null

    override fun switchToHomeFragment() {
        val homeFragment = HomeFragment()
        replaceFragment(homeFragment, NavigationEvent.HOME.titleStrRes)
    }

    override fun switchToEmptyHomeFragment() {
        val emptyHomeFragment = EmptyHomeFragment()
        replaceFragment(emptyHomeFragment, NavigationEvent.HOME.titleStrRes)
    }

    override fun switchToAboutFragment() {
        val aboutFragment = AboutFragment()
        replaceFragment(aboutFragment, NavigationEvent.ABOUT.titleStrRes)
    }

    override fun switchToSettingsFragment() {
        val settingsFragment = SettingsFragment()
        replaceFragment(settingsFragment, NavigationEvent.SETTINGS.titleStrRes)
    }

    override fun switchToAppsListFragment(listType: ListType) {
        val fragment = newAppsListFragmentInstance(listType.toAppListType(), listType)
        replaceFragment(fragment, listType.displayName)
    }

    override fun switchToAppsListFragment(searchFor: String) {
        val fragment = newAppsListFragmentInstance(searchFor)
        replaceFragment(fragment, titleStr = searchFor)
    }

    override fun refreshListFragment() {
        lastFragment?.let {
            if (it is AppsListFragment) {
                replaceFragment(it)
            }
        }
    }

    override fun showInputDialog() {
        MaterialDialog.Builder(this)
                .title(R.string.searchDialogHeader)
                .inputType(InputType.TYPE_CLASS_TEXT)
                .input(getString(R.string.searchDialogInputHint), "", { _, result -> inputEvents.onNext(result.toString()) })
                .positiveText(R.string.searchDialogSubmitAction)
                .negativeText(R.string.searchDialogCancelAction)
                .show()
    }

    override fun confirmUpdate() {
        MaterialDialog.Builder(this)
                .title(getString(R.string.mobileDataUpdateWarningTitle))
                .content(getString(R.string.mobileDataWarningContent))
                .positiveText(getString(R.string.mobileDataUpdateConfirm))
                .negativeText(getString(R.string.mobileDataUpdateCancel))
                .onPositive { _, _ -> eventBus.onNext(ViewEvent.UPDATE_CONFIRMED) }
                .show()
    }

    private fun replaceFragment(fragment: Fragment, title: Int = 0, remember: Boolean = true, titleStr: String = "") {
        var titleString = fragment.toString()
        if (title != 0) {
            titleString = getString(title)
        }
        if (!titleStr.isEmpty()) {
            titleString = titleStr
        }

        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.mainContainer, fragment)
        transaction.commit()

        materialCab?.toolbar?.title = titleString

        if (remember) {
            lastFragment = fragment
        }
    }
}

fun startMainActivity(context: Context) {
    val intent = Intent(context, MainActivity::class.java)
    context.startActivity(intent)
}

fun Drawable.changeIconColor(color: Int) {
    mutate()
    setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
}
