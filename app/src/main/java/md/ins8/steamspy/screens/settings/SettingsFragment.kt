package md.ins8.steamspy.screens.settings

import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.preference.Preference
import android.support.v7.preference.PreferenceFragmentCompat
import md.ins8.steamspy.AbstractAlarmManager
import md.ins8.steamspy.R
import md.ins8.steamspy.SteamSpyApp
import javax.inject.Inject

const val AUTOMATIC_UPDATE_PREFERENCE_KEY = "automatic_data_update"
const val AUTOMATIC_UPDATE_MOBILE_DATA_KEY = "automatic_data_update_on_mobile_data"
const val AUTOMATIC_UPDATE_LOW_BATTERY = "automatic_data_update_on_low_battery"
const val AUTOMATIC_UPDATE_SLOW_CONNECTIVITY = "automatic_data_update_on_slow_connectivity"

class SettingsFragment : PreferenceFragmentCompat() {
    private lateinit var mobileData: Preference
    private lateinit var lowBattery: Preference
    private lateinit var slowConnectivity: Preference

    @Inject
    lateinit var abstractAlarmManager: AbstractAlarmManager

    override fun onCreatePreferences(savedInstanceState: Bundle?, p1: String?) {
        addPreferencesFromResource(R.xml.preferences)

        val enable = findPreference(AUTOMATIC_UPDATE_PREFERENCE_KEY)
        mobileData = findPreference(AUTOMATIC_UPDATE_MOBILE_DATA_KEY)
        lowBattery = findPreference(AUTOMATIC_UPDATE_LOW_BATTERY)
        slowConnectivity = findPreference(AUTOMATIC_UPDATE_SLOW_CONNECTIVITY)

        enable.onPreferenceChangeListener = Preference.OnPreferenceChangeListener { _, newValue ->
            val toUpdate = newValue as Boolean
            if (toUpdate) {
                if (!abstractAlarmManager.alreadySetup) {
                    abstractAlarmManager.setup()
                }
                enableAutoUpdatePreferences()
            } else {
                abstractAlarmManager.cancel()

                disableAutoUpdatePreferences()
            }
            true
        }

        if (PreferenceManager.getDefaultSharedPreferences(context).getBoolean(AUTOMATIC_UPDATE_PREFERENCE_KEY, false)) {
            enableAutoUpdatePreferences()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        DaggerSettingsComponent.builder().appComponent((activity.application as SteamSpyApp).appComponent).build().inject(this)
    }

    private fun enableAutoUpdatePreferences() {
        mobileData.isEnabled = true
        lowBattery.isEnabled = true
        slowConnectivity.isEnabled = true
    }

    private fun disableAutoUpdatePreferences() {
        mobileData.isEnabled = false
        lowBattery.isEnabled = false
        slowConnectivity.isEnabled = false
    }
}
