package md.ins8.steamspy.service.update

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.wifi.WifiManager
import android.os.BatteryManager
import android.preference.PreferenceManager
import com.facebook.network.connectionclass.ConnectionClassManager
import com.facebook.network.connectionclass.ConnectionQuality
import md.ins8.steamspy.screens.settings.AUTOMATIC_UPDATE_LOW_BATTERY
import md.ins8.steamspy.screens.settings.AUTOMATIC_UPDATE_MOBILE_DATA_KEY
import md.ins8.steamspy.screens.settings.AUTOMATIC_UPDATE_PREFERENCE_KEY
import md.ins8.steamspy.screens.settings.AUTOMATIC_UPDATE_SLOW_CONNECTIVITY


class AutoUpdateConditionsChecker(val context: Context) {
    private val mBatInfoReceiver = object : BroadcastReceiver() {
        override fun onReceive(ctxt: Context, intent: Intent) {
            val level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0)
            val scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, 0)
            val batteryPct = level / scale.toFloat()
            batteryPercent = (batteryPct * 100).toInt()
        }
    }
    private val prefs = PreferenceManager.getDefaultSharedPreferences(context)
    private var batteryPercent: Int = 0

    init {
        context.registerReceiver(this.mBatInfoReceiver, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
    }

    fun check(): Boolean {
        if (!checkAutoUpdateEnabled()) {
            return false
        }
        if (!onWifi()) {
            if (!checkUpdateMobileData()) {
                return false
            }
        }
        if (batteryPercent < 20) {
            if (!checkUpdateEvenWhenBatteryLow()) {
                return false
            }
        }
        if (connectionIsSlow()) {
            if (!checkUpdateSlowConnectivity()) {
                return false
            }
        }
        return true
    }

    private fun checkAutoUpdateEnabled(): Boolean =
            prefs.getBoolean(AUTOMATIC_UPDATE_PREFERENCE_KEY, false)

    private fun checkUpdateEvenWhenBatteryLow(): Boolean =
            prefs.getBoolean(AUTOMATIC_UPDATE_LOW_BATTERY, false)

    private fun checkUpdateMobileData(): Boolean =
            prefs.getBoolean(AUTOMATIC_UPDATE_MOBILE_DATA_KEY, false)

    private fun checkUpdateSlowConnectivity(): Boolean =
            prefs.getBoolean(AUTOMATIC_UPDATE_SLOW_CONNECTIVITY, false)

    private fun onWifi(): Boolean {
        val wifi = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
        return wifi.isWifiEnabled
    }

    private fun connectionIsSlow(): Boolean {
        val cq = ConnectionClassManager.getInstance().currentBandwidthQuality
        return !(cq == ConnectionQuality.POOR || cq == ConnectionQuality.UNKNOWN)
    }
}
