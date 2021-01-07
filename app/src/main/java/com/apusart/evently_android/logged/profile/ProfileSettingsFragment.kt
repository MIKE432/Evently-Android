package com.apusart.evently_android.logged.profile

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.apusart.evently_android.R

class ProfileSettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.profile_settings, rootKey)
    }
}