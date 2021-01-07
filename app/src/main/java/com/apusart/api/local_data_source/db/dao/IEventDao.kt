package com.apusart.api.local_data_source.db.dao

import androidx.room.*
import com.apusart.api.ProfilePhoto

@Dao
interface IEventDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveProfilePhoto(profilePhoto: ProfilePhoto)
    @Query("SELECT photoURI FROM profilePhotoTable WHERE uid = :iemail Limit 1")
    suspend fun getProfilePhoto(iemail:String): String
}