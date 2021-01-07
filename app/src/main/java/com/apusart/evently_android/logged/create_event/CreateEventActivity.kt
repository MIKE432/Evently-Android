package com.apusart.evently_android.logged.create_event

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import com.apusart.api.handleResource
import com.apusart.appComponent
import com.apusart.evently_android.EventPageDestination
import com.apusart.evently_android.R
import com.apusart.evently_android.databinding.CreateEventActivityBinding
import com.apusart.tools.Codes
import kotlinx.android.synthetic.main.create_event_activity.*

//data class musi być bo nazwa headera, nazwa buttona i aktualna destynacja


class CreateEventActivity : AppCompatActivity() {
    private val destinations = listOf(
        EventPageDestination("Event Details", "", R.id.AddEventDetailsFragment, "Next"),
        EventPageDestination("Place and time", "", R.id.AddEventPlaceTimeFragment, "Next"),
        EventPageDestination("Category", "", R.id.AddEventPickCategoryFragment, "Next"),
        EventPageDestination("Preview", "", R.id.AddEventPreviewFragment, "Create event")
    )

    val viewModel: CreateEventViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: CreateEventActivityBinding =
            DataBindingUtil.setContentView(this, R.layout.create_event_activity)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        setUpObservers()

        create_event_activity_button_next.setOnClickListener {
            if (viewModel.currentPage.value == viewModel.max - 1)
                viewModel.createEvent()
            else
                create_event_activity_button_next.isDisabled = !viewModel.increment()
        }

        create_event_activity_header.setOnLeadingIconClickListener {
            if (viewModel.currentPage.value == 0)
                finish()
            else {
                findNavController(R.id.create_event_activity_fragment_container).popBackStack()
                create_event_activity_button_next.isDisabled = !viewModel.decrement()
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()

        if (viewModel.currentPage.value == 0) {
            finish()
        } else {
            create_event_activity_button_next.isDisabled = !viewModel.decrement()
        }
    }

    private fun setUpObservers() {
        viewModel.createdEventId.observe(this, { res ->
            handleResource(res,
                onSuccess = {
                    finish()
                }, onPending = {
                    create_event_activity_button_next.transitionToEnd()
                }, onError = { msg, _ ->
                    create_event_activity_button_next.transitionToStart()
                    Log.e("createEvent", msg ?: "")
                })
        })

        viewModel.currentPage.observe(this, {
            val destination = destinations[it]

            findNavController(R.id.create_event_activity_fragment_container).navigate(destinations[it].destination)
            create_event_activity_header.title = destination.title
            create_event_activity_header.subtitle = destination.subtitle
            create_event_activity_button_next.title = destination.buttonText
        })
    }

}