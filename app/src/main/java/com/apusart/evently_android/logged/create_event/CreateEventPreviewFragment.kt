package com.apusart.evently_android.logged.create_event

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.apusart.evently_android.R
import com.apusart.evently_android.databinding.CreateEventPreviewBinding
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.create_event_preview.*

class CreateEventPreviewFragment: Fragment() {

    val viewModel: CreateEventViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: CreateEventPreviewBinding = DataBindingUtil.inflate(inflater, R.layout.create_event_preview, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Glide.with(this)
            .load(viewModel.photoUri.value)
            .into(create_event_preview_cover_photo)
    }
}