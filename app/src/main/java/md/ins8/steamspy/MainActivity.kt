package md.ins8.steamspy

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import co.zsmb.materialdrawerkt.builders.drawer
import co.zsmb.materialdrawerkt.draweritems.badgeable.primaryItem
import co.zsmb.materialdrawerkt.draweritems.badgeable.secondaryItem
import co.zsmb.materialdrawerkt.draweritems.divider
import co.zsmb.materialdrawerkt.draweritems.expandable.expandableItem
import com.afollestad.materialcab.MaterialCab

class MainActivity : AppCompatActivity() {

    private fun onHome() {

    }

    private fun onAll() {

    }

    private fun onTop2Weeks() {

    }

    private fun onOwned() {}

    private fun onTotal() {}

    private fun onNotifications() {}

    private fun onSettings() {}

    private fun onAbout() {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val materialCab = MaterialCab(this, R.id.cab_stub).start(object : MaterialCab.Callback {
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

        drawer {
            headerViewRes = R.layout.drawer_header
            toolbar = materialCab.toolbar
            primaryItem(R.string.navigation_home) {
                onClick { _ -> onHome(); false }
                icon = R.drawable.ic_home_black_24dp
            }
            primaryItem(R.string.navigation_all) {
                onClick { _ -> onAll(); false }
                icon = R.drawable.ic_view_list_black_24dp
            }
            divider { }
            expandableItem(R.string.navigation_top) {
                secondaryItem(getString(R.string.navTop2Weeks)) {
                    onClick { _ -> onTop2Weeks(); false }
                    icon = R.drawable.ic_top_2w
                }
                secondaryItem(getString(R.string.navTopOwned)) {
                    icon = R.drawable.ic_ow
                }
                secondaryItem(getString(R.string.navTopTotal)) {
                    icon = R.drawable.ic_tl
                }
                selectable = false
                icon = R.drawable.ic_call_made_black_24dp
            }
            divider { }
            expandableItem(R.string.navigation_genre) {
                secondaryItem(getString(R.string.navGenreAction)) {
                    icon = R.drawable.ic_ac
                }
                secondaryItem(getString(R.string.navGenreStrategy)) {
                    icon = R.drawable.ic_st
                }
                secondaryItem(getString(R.string.navGenreRPG)) {
                    icon = R.drawable.ic_rp
                }
                secondaryItem(getString(R.string.navGenreIndie)) {
                    icon = R.drawable.ic_in
                }
                secondaryItem(getString(R.string.navGenreAdventure)) {
                    icon = R.drawable.ic_ad
                }
                secondaryItem(getString(R.string.navGenreSports)) {
                    icon = R.drawable.ic_sp
                }
                secondaryItem(getString(R.string.navGenreSimulation)) {
                    icon = R.drawable.ic_sm
                }
                secondaryItem(getString(R.string.navGenreEarlyAccess)) {
                    icon = R.drawable.ic_ea
                }
                secondaryItem(getString(R.string.navGenreExEarlyAccess)) {
                    icon = R.drawable.ic_ee
                }
                secondaryItem(getString(R.string.navGenreMMO)) {
                    icon = R.drawable.ic_mm
                }
                secondaryItem(getString(R.string.navGenreFree)) {
                    icon = R.drawable.ic_fr
                }
                selectable = false
                icon = R.drawable.ic_star_black_24dp
            }
            divider { }
            primaryItem(R.string.navigation_notifications) {
                icon = R.drawable.ic_notifications_black_24dp
            }
            primaryItem(R.string.navigation_settings) {
                icon = R.drawable.ic_settings_black_24dp
            }
            primaryItem(R.string.navigation_about) {
                icon = R.drawable.ic_info_black_24dp
            }
        }


    }
}
