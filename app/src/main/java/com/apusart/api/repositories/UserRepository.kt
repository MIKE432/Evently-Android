package com.apusart.api.repositories

import android.content.Context
import android.net.Uri
import android.util.Log
import com.apusart.api.Resource
import com.apusart.api.remote_data_source.services.UserService
import com.facebook.AccessToken
import com.facebook.login.LoginManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.ktx.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.util.*
import kotlin.collections.HashMap

class UserRepository(private val context: Context) {
    private val gto = GoogleSignInOptions
        .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .build()
    private val gsc = GoogleSignIn.getClient(context, gto)
    private val userService = UserService()
    private lateinit var auth: FirebaseAuth

    suspend fun facebookLogIn(token: AccessToken): Resource<FirebaseUser> {
        val credential = FacebookAuthProvider.getCredential(token.token)
        Firebase.auth.signInWithCredential(credential).await()
        return user()
    }

    suspend fun googleLogIn(idToken: String): Resource<FirebaseUser> {
        val credentials = GoogleAuthProvider.getCredential(idToken, null)
        Firebase.auth.signInWithCredential(credentials).await()
        return user()
    }

    suspend fun emailLogIn(email: String, password: String): Resource<FirebaseUser> {
        Firebase.auth.signInWithEmailAndPassword(email, password).await()
        return user()
    }

    suspend fun register(email: String, name: String, password: String): Resource<FirebaseUser> {
        auth = Firebase.auth
        var callback = auth.createUserWithEmailAndPassword(email, password).await()

        if (callback.user?.email != null) {
            val userID = Firebase.auth.currentUser?.uid
            val docRef =
                FirebaseFirestore.getInstance().collection("users")
                    .document(userID.toString())
            
            val id = generate_id(name)
            val user = hashMapOf(
                "name" to name,
                "id" to id,
                "email" to email
            )
            docRef.set(user)
            return user()
        } else {
            return Resource.error("Problem with registration")
        }

    }

    private fun user(): Resource<FirebaseUser> {
        val currUser = Firebase.auth.currentUser
        if (currUser != null) {
            return Resource.success(currUser)
        } else {
            return Resource.error("Cannot be logged")
        }
    }

    fun signOut(): Resource<Boolean> {
        gsc.signOut()
        Firebase.auth.signOut()
        LoginManager.getInstance().logOut()
        return Resource.success(true)
    }

    suspend fun uploadProfilePhoto(uri: Uri, id: String, pictureName: String) {
        val file = userService.getReference("users/${id}/${pictureName}.jpg")
        file.putFile(uri).await()
    }

    suspend fun getDownloadLink(path: String): Resource<Uri> {
        val file = userService.getReference(path)
        return Resource.success(file.downloadUrl.await())
    }

    private fun generate_id(name: String): String {
        var id = ""
        id = name.toLowerCase()
        id.replace(" ", ".")
        return id
    }
}