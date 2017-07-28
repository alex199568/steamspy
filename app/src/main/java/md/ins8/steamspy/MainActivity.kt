package md.ins8.steamspy

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import co.zsmb.materialdrawerkt.builders.drawer
import co.zsmb.materialdrawerkt.draweritems.badgeable.primaryItem
import co.zsmb.materialdrawerkt.draweritems.badgeable.secondaryItem
import co.zsmb.materialdrawerkt.draweritems.divider
import co.zsmb.materialdrawerkt.draweritems.expandable.expandableItem

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

        drawer {
            headerViewRes = R.layout.drawer_header
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
