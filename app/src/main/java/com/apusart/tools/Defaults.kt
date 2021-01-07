package com.apusart.tools

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import com.apusart.api.Resource
import com.apusart.api.User
import com.apusart.evently_android.MainActivity
import com.apusart.evently_android.R
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseUser

import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.concurrent.CancellationException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

object Defaults {
    const val baseUrl = "" //base url
}

object Tools {
    fun hideKeyboard(fragment: Fragment?) {
        val view = fragment?.requireActivity()?.currentFocus
        view?.let { v ->
            val imm = fragment.requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(v.windowToken, 0)
        }
    }

    fun hideKeyboard(activity: Activity?) {
        val view = activity?.currentFocus
        view?.let { v ->
            val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(v.windowToken, 0)
        }
    }

    fun toStringDate(day: Int, month: Int, year: Int, hour: Int, minutes: Int): String {
        return "${zeroInFrontOf(day)}-${zeroInFrontOf(month)}-$year at ${zeroInFrontOf(hour)}:${zeroInFrontOf(minutes)}"
    }

    private fun zeroInFrontOf(int: Int): String {
        return if(int >=10) "$int" else "0$int"
    }


    fun getRandomString(length: Int) : String {
        val charset = "ABCDEFGHIJKLMNOPQRSTUVWXTZabcdefghiklmnopqrstuvwxyz0123456789"
        return (1..length)
            .map { charset.random() }
            .joinToString("")
    }
}

object LoginTools {
    lateinit var context: Context
    val gso by lazy {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
    }

    fun provideContext(c: Context) {
        context = c
    }
}

object Codes {
    const val GET_PHOTO_CODE = 1
    const val RC_SIGN_IN = 2
}