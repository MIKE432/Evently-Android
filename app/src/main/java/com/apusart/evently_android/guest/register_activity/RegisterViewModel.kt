package com.apusart.evently_android.guest.register_activity

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apusart.api.Resource
import com.apusart.api.repositories.UserRepository
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import javax.inject.Inject

class RegisterActivityViewModel(private val context: Context) : ViewModel() {
    private val userRepository = UserRepository(context)
    val emailText = MutableLiveData<String>()
    val nameText = MutableLiveData<String>()
    val passwordText = MutableLiveData<String>()
    val user = MutableLiveData<Resource<FirebaseUser>>()

    fun register() {
        viewModelScope.launch {
            try {
                if (emailText.value == null || emailText.value.equals("")) {
                    return@launch
                }

                print(nameText.value)

                if (nameText.value == null || nameText.value.equals("")) {
                    return@launch
                }

                if (passwordText.value == null || passwordText.value.equals("")) {
                    return@launch
                }
                user.value = Resource.pending()
                user.value = userRepository.register(emailText.value!!, nameText.value!!, passwordText.value!!)
            } catch (e: Exception) {
                user.value = Resource.error(e.message)
            }
        }
    }
}