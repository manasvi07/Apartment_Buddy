package com.example.apartmentbuddy.fragments

import android.content.ContentValues.TAG
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toolbar
import androidx.annotation.RequiresApi
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.apartmentbuddy.R
import com.example.apartmentbuddy.model.Appointment
import com.example.apartmentbuddy.model.FirebaseAuthUser
import com.google.android.material.textfield.TextInputLayout

/**
 * A simple [Fragment] subclass.
 * Use the [AppointmentNotes.newInstance] factory method to
 * create an instance of this fragment.
 */
class AppointmentNotes : Fragment() {
    private val appointment = Appointment()
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_appointment_notes, container, false)
        val submit : Button = view.findViewById(R.id.notes_appointment_submit)
        val selected_date = AppointmentNotesArgs.fromBundle(requireArguments()).date
        val selected_time = AppointmentNotesArgs.fromBundle(requireArguments()).time
        var user_id : String = FirebaseAuthUser.getUserEmail().toString()
        var uid : String = FirebaseAuthUser.getUserId().toString()
        var user_name : String = "null"
        appointment.getUserName(uid){
            user_name = it
        }

        val myToolbar: Toolbar = view.findViewById(R.id.toolbar) as Toolbar
        myToolbar.inflateMenu(R.menu.appointment_new)
        myToolbar.title = "New Appointment"
        myToolbar.setTitleTextAppearance(this.context,R.style.CustomActionBarStyle)
        myToolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
        myToolbar.setNavigationOnClickListener { view ->
            view.findNavController().navigate(AppointmentNotesDirections.actionAppointmentNotesToNewAppointment())
        }
        myToolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_home -> {
                    // TODO: Navigate to HOME PAGE
                    findNavController().navigate(R.id.action_global_home22)
                    true
                }
                else -> false
            }
        }

        submit.setOnClickListener {
            val getNotes : EditText = view.findViewById(R.id.appointment_notes)
            val notes : String = getNotes.text.toString()
            this.view?.let { it1 ->
                appointment.confirmAppointment(
                    selected_date, selected_time, context,
                    it1, user_id, user_name, notes
                )
            }

        }

        val back : Button = view.findViewById(R.id.notes_appointment_back)
        back.setOnClickListener {
            view.findNavController().navigate(AppointmentNotesDirections.actionAppointmentNotesToAppointmentHome())
        }
        return view
    }
}