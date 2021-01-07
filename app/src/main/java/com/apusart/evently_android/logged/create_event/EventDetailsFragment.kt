package com.apusart.evently_android.logged.create_event

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.apusart.evently_android.R
import com.apusart.evently_android.databinding.AddEventDetailsBinding
import com.apusart.tools.Codes
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.add_event_details.*

class EventDetailsFragment: Fragment() {
    private val viewModel: CreateEventViewModel by activityViewModels()
    private lateinit var binding: AddEventDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.add_event_details, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        add_event_details_event_text.setOnClickListener {
            startActivityForResult(Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI), Codes.GET_PHOTO_CODE)
        }

        viewModel.photoUri.observe(viewLifecycleOwner, {
            Glide.with(this)
                .load(it)
                .centerCrop()
                .into(add_event_details_event_add_photo)
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Codes.GET_PHOTO_CODE) {
            val imageUri = data?.data
            if (imageUri != null)
                viewModel.setPhotoUri(imageUri)

        }
    }
}