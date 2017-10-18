package md.ins8.steamspy.screens.settings

import android.os.Bundle
import android.support.v7.preference.PreferenceFragmentCompat
import md.ins8.steamspy.AbstractAlarmManager
import md.ins8.steamspy.R
import md.ins8.steamspy.SteamSpyApp
import javax.inject.Inject

const val AUTOMATIC_UPDATE_PREFERENCE_KEY = "automatic_data_update"

class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, p1: String?) {
        addPreferencesFromResource(R.xml.preferences)

        val pref = findPreference(AUTOMATIC_UPDATE_PREFERENCE_KEY)
        pref.onPreferenceChangeListener = android.support.v7.preference.Preference.OnPreferenceChangeListener { _, newValue ->
            val toUpdate = newValue as Boolean
            if (toUpdate) {
                if (!abstractAlarmManager.alreadySetup) {
                    abstractAlarmManager.setup()
                }
            } else {
                abstractAlarmManager.cancel()
            }
            true
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        DaggerSettingsComponent.builder().appComponent((activity.application as SteamSpyApp).appComponent).build().inject(this)
    }

    @Inject
    lateinit var abstractAlarmManager: AbstractAlarmManager
}
