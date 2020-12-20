package com.apusart.evently_android.guest.reset_password_activity

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apusart.api.Resource
import com.apusart.api.repositories.UserRepository
import kotlinx.coroutines.launch
import java.lang.Exception

class ResetPasswordViewModel(private val context: Context) : ViewModel() {
    val emailText = MutableLiveData<String>()
    val userRepository = UserRepository(context)
    val resetPasswordResponse = MutableLiveData<Resource<Boolean>>()

    fun resetPassword() {
        viewModelScope.launch {
            try {
                if (emailText.value == null || emailText.value.equals("")) {
                    return@launch
                }

                resetPasswordResponse.value = Resource.pending()
                resetPasswordResponse.value = userRepository.resetPassword(emailText.value!!)

            } catch (e: Exception) {
                resetPasswordResponse.value = Resource.error(e.message)
            }
        }
    }
}
