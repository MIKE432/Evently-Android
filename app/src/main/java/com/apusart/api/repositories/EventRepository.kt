package com.apusart.api.repositories

import android.net.Uri
import com.apusart.api.local_data_source.db.services.EventLocalService
import com.apusart.api.remote_data_source.services.EventRemoteService
import javax.inject.Inject

class EventRepository{
    private val eventLocalService = EventLocalService()

    suspend fun saveProfilePhoto(email:String,img:String){
        eventLocalService.saveProfilePhoto(email,img)
    }
    suspend fun getProfilePhoto(email:String): Uri? {
        return Uri.parse(eventLocalService.getProfilePhoto(email))
    }
}