package com.apusart.evently_android.logged.create_event

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apusart.api.Resource
import com.apusart.api.UserShort
import com.apusart.api.repositories.EventRepository
import com.apusart.evently_android.Category
import com.google.android.gms.auth.api.Auth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import java.lang.Exception

class CreateEventViewModel : ViewModel() {
    val currentPage = MutableLiveData(0)
    val eventName = MutableLiveData<String>()
    val eventDescription = MutableLiveData<String>()
    val date = MutableLiveData<String>()
    val place = MutableLiveData<String>()
    val max = 4
    val creator = MutableLiveData<UserShort>()
    val categories = listOf(
        Category("domówka"),
        Category("klub"),
        Category("kolacja"),
        Category("grill"),
        Category("urodziny"),
        Category("integracja"),
        Category("plener")
    )

    val photoUri = MutableLiveData<Uri>()
    val eventRepository = EventRepository()
    val createdEventId = MutableLiveData<Resource<String>>()


    init {
        val currentUser = Firebase.auth.currentUser
        val id = currentUser?.uid ?: "."
        val name = currentUser?.displayName ?: currentUser?.email ?: "No information"
        creator.value = UserShort(id, name)
    }

    fun increment(): Boolean {
        currentPage.value = currentPage.value?.plus(1)?.rem(6)
        return (currentPage.value != max - 1)
                || ((eventName.value != null)
                    && (eventDescription.value != null)
                    && (date.value != null)
                    && (place.value != null)
                    && (creator.value != null))
    }

    fun decrement(): Boolean {
        currentPage.value = currentPage.value?.minus(1)?.rem(6)

        return true
    }

    fun setPhotoUri(uri: Uri) {
        photoUri.value = uri
    }

    fun createEvent() {
        viewModelScope.launch {
            try {
                createdEventId.value = Resource.pending()
                createdEventId.value = eventRepository.createEvent(
                    eventName.value!!,
                    eventDescription.value!!,
                    date.value!!,
                    place.value!!,
                    creator.value!!,
                    photoUri.value,
                    categories.filter { it.isChecked }.map { it.name })
            } catch (e: Exception) {
                createdEventId.value = Resource.error(e.message)
            }
        }
    }
}