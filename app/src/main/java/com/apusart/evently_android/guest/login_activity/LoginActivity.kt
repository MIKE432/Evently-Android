package com.apusart.evently_android.guest.login_activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.telecom.Call
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.apusart.api.Resource
import com.apusart.api.handleResource
import com.apusart.appComponent
import com.apusart.evently_android.R
import com.apusart.evently_android.databinding.LoginBinding
import com.apusart.evently_android.guest.register_activity.RegisterActivity
import com.apusart.evently_android.guest.reset_password_activity.ResetPasswordActivity
import com.apusart.evently_android.logged.main.MainLoggedActivity
import com.apusart.tools.AppViewModelFactory
import com.apusart.tools.Codes
import com.apusart.tools.LoginTools
import com.apusart.tools.Tools
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.FacebookSdk
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.login.*
import kotlinx.android.synthetic.main.login.view.*
import kotlinx.android.synthetic.main.register.*
import javax.inject.Inject

class LoginActivity: AppCompatActivity() {

    private val viewModel: LoginActivityViewModel by viewModels { AppViewModelFactory(this) }
    lateinit var callbackManager: CallbackManager

    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        FacebookSdk.sdkInitialize(this)
        super.onCreate(savedInstanceState)

        val binding: LoginBinding = DataBindingUtil.setContentView(this, R.layout.login)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        login_email_login_button.setOnClickListener {
            Tools.hideKeyboard(this)
            viewModel.logIn()
        }

        login_register_text.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION))
        }

        viewModel.user.observe(this, { res ->
            handleResource(res, {
                startActivity(
                    Intent(this, MainLoggedActivity::class.java)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                )
            }, {
                login_email_login_button.transitionToEnd()
            }, { msg, _ ->
                login_email_login_button.transitionToStart()
                login_error_modal.modalInformation = msg
                login_error_modal.isActive = true
            })
        })

        googleSignInClient = GoogleSignIn.getClient(this, LoginTools.gso)
        callbackManager = CallbackManager.Factory.create()

        login_email_facebook_button.setReadPermissions("email", "public_profile")
        login_email_facebook_button.registerCallback(callbackManager, object: FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult?) {
                viewModel.fbLogin(result?.accessToken!!)
            }

            override fun onCancel() {
                Log.v("TAG", "CANCEL")
            }

            override fun onError(error: FacebookException?) {
                Log.v("TAG", "ERROR")
            }
        })

        login_email_google_button_container.setOnClickListener {
            googleSignIn()
        }

        login_email_facebook_button_container.setOnClickListener {
            login_email_facebook_button.performClick()
        }

        login_forgot_password.setOnClickListener {
            startActivity(Intent(this, ResetPasswordActivity::class.java)
            .addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION))
        }
    }

    private fun googleSignIn() {
        val intent = googleSignInClient.signInIntent
        startActivityForResult(intent, Codes.RC_SIGN_IN)
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
       viewModel.googleLogIn(idToken)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Codes.RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account?.idToken!!)
            } catch(e: ApiException) {
                login_error_modal.isActive = true
                login_error_modal.modalInformation = e.message.toString()
            }
        }
    }
}