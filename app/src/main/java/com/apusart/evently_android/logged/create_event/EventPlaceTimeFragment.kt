package com.apusart.evently_android.logged.create_event

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.apusart.components.MainInput
import com.apusart.evently_android.MainActivity
import com.apusart.evently_android.R
import com.apusart.evently_android.databinding.AddEventDetailsBinding
import com.apusart.evently_android.databinding.AddEventPlaceTimeBinding
import com.apusart.tools.Tools
import kotlinx.android.synthetic.main.add_event_place_time.*
import java.util.*

class EventPlaceTimeFragment : Fragment() {
    private val viewModel: CreateEventViewModel by activityViewModels()
    private lateinit var binding: AddEventPlaceTimeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.add_event_place_time, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupListeners()
    }

    private fun setupListeners() {
        add_event_details_event_time.isDisabled = true
        add_event_details_event_time.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            DatePickerDialog(
                requireContext(),
                { _, _year, _month, _dayOfMonth ->
                    val hour = calendar.get(Calendar.HOUR_OF_DAY)
                    val minutes = calendar.get(Calendar.MINUTE)

                    TimePickerDialog(
                        requireActivity(),
                        { _, _hourOfDay, _minute ->
                            viewModel.date.value =
                                Tools.toStringDate(_dayOfMonth, _month + 1, _year, _hourOfDay, _minute)

                        }, hour, minutes, true
                    ).show()
                }, year, month, day
            ).show()
        }
    }
}