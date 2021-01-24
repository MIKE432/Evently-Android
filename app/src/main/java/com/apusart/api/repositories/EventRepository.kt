package com.apusart.api.repositories

import com.apusart.api.Event
import com.apusart.api.local_data_source.db.services.EventLocalService
import com.apusart.api.remote_data_source.services.EventRemoteService
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.apusart.api.Resource
import com.apusart.api.local_data_source.db.EventlyDatabase.Companion.db
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import javax.inject.Inject

class EventRepository @Inject constructor(
    private val eventLocalService: EventLocalService,
    private val eventRemoteDataService: EventRemoteService,
    private val db: FirebaseFirestore = Firebase.firestore
) {

    fun attend(event: Event) : Boolean {
        val currUser = Firebase.auth.currentUser
        val docRef =
            FirebaseFirestore.getInstance().collection("events")
                .document(event.id.toString()) ?: return false

//        val attendees: HashMap<Int, String> = HashMap<Int, String> ()
//        docRef.get().addOnSuccessListener { document ->
//            if (document != null){
//                for (i in document.data("attendees")){
//
//                }
//            }
//        }
//        attendees.put(currUser.hashCode(), decision)
        docRef.update("attendees", FieldValue.arrayUnion(currUser.hashCode()))
        return true
    }

}