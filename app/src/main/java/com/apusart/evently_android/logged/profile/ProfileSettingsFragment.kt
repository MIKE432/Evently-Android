package com.apusart.evently_android.logged.profile

import android.os.Bundle
import android.text.InputType
import androidx.preference.EditTextPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.apusart.api.repositories.UserRepository
import com.apusart.evently_android.R
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ProfileSettingsFragment : PreferenceFragmentCompat() {

    //private val userRepository = context?.let { UserRepository(it) }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.profile_settings, rootKey)


        val userEmailPreference: EditTextPreference? = findPreference("profile_user_email")

        val firebaseEmail = Firebase.auth.currentUser?.email
        userEmailPreference?.text = firebaseEmail

        userEmailPreference?.summaryProvider = Preference.SummaryProvider<EditTextPreference> { preference ->
            val email = preference.text
            var retValue = false
            if (email != firebaseEmail) {
//                retValue = userRepository?.updateEmail(email) ?: false
                val currUser = Firebase.auth.currentUser
                currUser!!.updateEmail(email).addOnCompleteListener { task ->
                    retValue = task.isSuccessful
                }
            }
            if(retValue) email
            else firebaseEmail
        }
        userEmailPreference?.setOnBindEditTextListener { editText ->
            editText.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
        }

        //Dodac to samo co wyzej dla innych parametrow usera
    }
}