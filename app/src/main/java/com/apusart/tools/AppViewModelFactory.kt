package com.apusart.tools

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.apusart.evently_android.guest.login_activity.LoginActivityViewModel
import com.apusart.evently_android.guest.register_activity.RegisterActivityViewModel
import com.apusart.evently_android.guest.reset_password_activity.ResetPasswordViewModel
import com.apusart.evently_android.logged.profile.ProfileFragmentViewModel
import java.lang.Exception

class AppViewModelFactory(private val context: Context): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when(modelClass) {
            ProfileFragmentViewModel::class.java -> ProfileFragmentViewModel(context) as T
            LoginActivityViewModel::class.java -> LoginActivityViewModel(context) as T
            RegisterActivityViewModel::class.java -> RegisterActivityViewModel(context) as T
            ResetPasswordViewModel::class.java -> ResetPasswordViewModel(context) as T
            else -> throw Exception("Invalid class")
        }
    }
}