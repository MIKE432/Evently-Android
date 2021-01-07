package com.apusart.evently_android.logged.profile

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import com.apusart.api.Resource
import com.apusart.api.handleResource
import com.apusart.appComponent
import com.apusart.evently_android.R
import com.apusart.evently_android.guest.initial_activity.InitialActivity
import com.apusart.tools.AppViewModelFactory
import com.apusart.tools.Codes
import com.bumptech.glide.Glide
import com.facebook.FacebookSdk
import com.facebook.login.LoginManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.rpc.Code
import kotlinx.android.synthetic.main.profile_fragment.*

class ProfileFragment: Fragment(R.layout.profile_fragment) {
    val viewModel: ProfileFragmentViewModel by viewModels { AppViewModelFactory(requireContext()) }
    private lateinit var alertDialog: AlertDialog.Builder



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        requireContext().appComponent.inject(this)
        super.onViewCreated(view, savedInstanceState)
        viewModel.profilePicture.observe(viewLifecycleOwner, { res ->
            handleResource(res,
                onSuccess = {
                    profile_page_user_picture.loadPhoto(it)
                })
        })


        alertDialog = AlertDialog.Builder(requireContext())
        alertDialog
            .setTitle(getString(R.string.sign_out))
            .setMessage(getString(R.string.wanna_sign_out))
            .setPositiveButton(R.string.sign_out) { _, _ ->
                viewModel.signOut()
            }
            .setNegativeButton(R.string.abort) { dialog, _ ->
                dialog.cancel()
            }

        profile_page_settings_button.setOnClickListener {
            startActivity(Intent(context, ProfileSettingsActivity::class.java))
        }

        profile_page_logout_button.setOnClickListener {
            alertDialog.show()
        }

        profile_page_user_picture.setOnChangePictureListener {
            startActivityForResult(Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI), Codes.GET_PHOTO_CODE)
        }

        viewModel.isSignedOut.observe(viewLifecycleOwner, { res ->
            handleResource(res,
                onSuccess = {
                    startActivity(Intent(requireContext(), InitialActivity::class.java)
                        .addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION))
                    requireActivity().finishAffinity()
                },
                onPending = {
                    //logged out
                },
                onError = { _, _ ->
                    //logged out
                })
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == Codes.GET_PHOTO_CODE) {
            val imageUri = data?.data
            if (imageUri != null)
                viewModel.uploadImageToFirebase(imageUri)
        }
    }
}