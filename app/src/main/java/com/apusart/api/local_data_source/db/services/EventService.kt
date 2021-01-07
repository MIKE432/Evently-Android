package com.apusart.api.local_data_source.db.services

import com.apusart.api.ProfilePhoto
import com.apusart.api.local_data_source.db.EventlyDatabase
import javax.inject.Inject

class EventLocalService {
    private val db = EventlyDatabase.db
    suspend fun saveProfilePhoto(email: String,image: String) {
        val photo = ProfilePhoto(email, image)
        db.eventDao().saveProfilePhoto(photo)
    }


    suspend fun getProfilePhoto(email:String): String? {
        return db.eventDao().getProfilePhoto(email)
    }


}