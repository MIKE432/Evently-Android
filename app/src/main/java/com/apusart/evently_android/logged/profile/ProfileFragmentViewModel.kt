package com.apusart.evently_android.logged.profile

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.TextView

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apusart.api.Resource
import com.apusart.api.local_data_source.db.EventlyDatabase
import com.apusart.api.local_data_source.db.services.EventLocalService
import com.apusart.api.repositories.EventRepository
import com.apusart.api.repositories.UserRepository
import com.apusart.evently_android.R
import com.facebook.Profile
import com.facebook.login.LoginManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import javax.inject.Inject

class ProfileFragmentViewModel constructor(context: Context): ViewModel() {
    private val userRepository = UserRepository(context)
    private  val eventRepository = EventRepository()
    val currentUser = MutableLiveData<Resource<FirebaseUser?>>()
    val profilePicture = MutableLiveData(Resource.success(Uri.parse(Firebase.auth.currentUser?.photoUrl.toString() + "?height=500")))
    val isSignedOut = MutableLiveData<Resource<Boolean>>()
    val photoUri = MutableLiveData<String?>(null)
    val profileName = MutableLiveData<String>("")

    fun signOut() {
        viewModelScope.launch {
            try {
                currentUser.value = Resource.pending()
                isSignedOut.value = userRepository.signOut()
            } catch (e: Exception) {
                currentUser.value = Resource.error(e.message)
            }
        }
    }

    fun uploadImageToFirebase(uri: Uri) {
        viewModelScope.launch {
            try {
                val id = Firebase.auth.currentUser?.uid
                val pictureName = "profilePicture"
                if(id != null) {
                    userRepository.uploadProfilePhoto(uri, id, pictureName)
                    profilePicture.value = userRepository.getDownloadLink("users/${id}/${pictureName}.jpg")
                }
            } catch (e: Exception) {
                profilePicture.value = Resource.error(e.message)
            }
        }
    }

    fun uploadProfileToLocalDB(email:String, img:String){
        viewModelScope.launch {
            try {
                eventRepository.saveProfilePhoto(email, img)
            }catch (e: Exception) {
            }
        }
    }

    fun getProfilePhoto (email:String) {
        viewModelScope.launch {
            try{
                var localphotoUri = eventRepository.getProfilePhoto(email).toString()
                photoUri.value = localphotoUri
            } catch (e: Exception) {
                photoUri.value = ""
            }
        }
    }

    fun updateProfileName(){
        viewModelScope.launch {
            try {
                if (profileName.value == "") {
                    if (Profile.getCurrentProfile() != null) {
                        profileName.value = Profile.getCurrentProfile().firstName + " " + Profile.getCurrentProfile().lastName
                    } else {
                        profileName.value = MutableLiveData(Firebase.auth.currentUser).value?.email.toString()
                    }
                }
            }catch (e: Exception){
                profileName.value = ""
            }
        }
    }
}