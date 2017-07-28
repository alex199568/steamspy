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
                cab?.setTitle("Home: Watchlist")
                return true
            }
        })


        drawer {
            headerViewRes = R.layout.drawer_header
            toolbar = materialCab.toolbar
            primaryItem(R.string.navigation_home) {
                onClick { _ -> onHome(); false }
            }
            primaryItem(R.string.navigation_all) {
                onClick { _ -> onAll(); false }
            }
            divider { }
            expandableItem(R.string.navigation_top) {
                secondaryItem("2 Weeks") {
                    onClick { _ -> onTop2Weeks(); false }
                }
                secondaryItem("Owned") { }
                secondaryItem("Total") { }
                selectable = false
            }
            divider { }
            expandableItem(R.string.navigation_genre) {
                secondaryItem("Action") { }
                secondaryItem("Strategy") { }
                secondaryItem("RPG") { }
                secondaryItem("Indie") { }
                secondaryItem("Adventure") { }
                secondaryItem("Sports") { }
                secondaryItem("Simulation") { }
                secondaryItem("Early Access") { }
                secondaryItem("Ex Early Access") { }
                secondaryItem("MMO") { }
                secondaryItem("Free") { }
                secondaryItem("RPG") { }
                selectable = false
            }
            divider { }
            primaryItem(R.string.navigation_notifications) { }
            primaryItem(R.string.navigation_settings) { }
            primaryItem(R.string.navigation_about) { }
        }


    }
}
