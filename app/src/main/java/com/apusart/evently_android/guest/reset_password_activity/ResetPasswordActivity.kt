package com.apusart.evently_android.guest.reset_password_activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.apusart.api.handleResource
import com.apusart.evently_android.R
import com.apusart.evently_android.databinding.ResetPasswordBinding
import com.apusart.evently_android.guest.login_activity.LoginActivity
import com.apusart.tools.AppViewModelFactory
import com.apusart.tools.Tools
import kotlinx.android.synthetic.main.reset_password.*

class ResetPasswordActivity : AppCompatActivity() {

    private val viewModel: ResetPasswordViewModel by viewModels { AppViewModelFactory(this) }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        val binding: ResetPasswordBinding =
            DataBindingUtil.setContentView(this, R.layout.reset_password)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this


        reset_password_email_continue_button.setOnClickListener {
            Tools.hideKeyboard(this)
            viewModel.resetPassword()
        }

        viewModel.resetPasswordResponse.observe(this, { res ->
            handleResource(res,
                onSuccess = {
                    Toast.makeText(
                        this,
                        "Check your email for reset password link",
                        Toast.LENGTH_SHORT
                    ).show()
                    startActivity(Intent(this, LoginActivity::class.java)
                        .addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION or Intent.FLAG_ACTIVITY_CLEAR_TASK))
                },
                onPending = {
                    reset_password_email_continue_button.transitionToEnd()
                },
                onError = { msg, _ ->
                    reset_password_email_continue_button.transitionToStart()
                    reset_password_error_modal.isActive = true
                    reset_password_error_modal.modalTitle = "Error"
                    reset_password_error_modal.modalInformation = msg
                })
        })


        reset_password_remembered_credentials_text.setOnClickListener {
            startActivity(
                Intent(this, LoginActivity::class.java)
                    .addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            )
        }
    }
}