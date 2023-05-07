package com.example.apartmentbuddy.model

import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.content.Context
import android.os.Build
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.navigation.findNavController
import com.example.apartmentbuddy.fragments.*
import com.google.firebase.firestore.FirebaseFirestore
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Appointment : IAppointment{

    private val db = FirebaseFirestore.getInstance()

    fun  printValidTime(hourOfDay: Int, minute : Int) : String {
        var hour = hourOfDay
        var am_pm = ""
        // AM_PM decider logic
        when { hour == 0 -> { hour += 12
            am_pm = "AM"
        }
            hour == 12 -> am_pm = "PM"
            hour > 12 -> { hour -= 12
                am_pm = "PM"
            }
            else -> am_pm = "AM"
        }
        val hourDay = if (hour < 10) "0" + hour else hour
        val min = if (minute < 10) "0" + minute else minute
        // display format of time
        return "$hourDay : $min $am_pm"
    }

    @RequiresApi(Build.VERSION_CODES.O)
    // Reference : https://www.programiz.com/kotlin-programming/examples/current-date-time
    // Format the current Date and time in "yyyy-MM-dd HH:mm:ss"
    fun buildTimeStamp() : String? {
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")
        val formatted = current.format(formatter)
        return formatted
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun confirmAppointment(date: String, time: String, context: Context?, view: View, user_id: String, user_name: String, notes : String){
        val builder = AlertDialog.Builder(context)
        //set title for alert dialog
        builder.setTitle("Confirm")
        //set message for alert dialog
        builder.setMessage("Do you want proceed with this appointment on $date at $time?")

        //performing positive action
        builder.setPositiveButton("Yes"){dialogInterface, which ->
            view.findNavController().navigate(AppointmentNotesDirections.actionAppointmentNotesToConfirmAppointment(date,time,user_id,notes))
            addNewAppointment(date, time, user_id, user_name, context, notes)
        }
        //performing cancel action
        builder.setNeutralButton("Cancel"){dialogInterface , which ->
            Toast.makeText(context,"Appointment cancelled",Toast.LENGTH_LONG).show()
            view.findNavController().navigate(AppointmentNotesDirections.actionAppointmentNotesToAppointmentHome())
        }
        //performing negative action
        builder.setNegativeButton("No"){dialogInterface, which ->
            Toast.makeText(context,"Please select Date and Time",Toast.LENGTH_LONG).show()
        }
        // Create the AlertDialog
        val alertDialog: AlertDialog = builder.create()
        // Set other dialog properties
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    fun confirmToCancelAppointment(appointmentId: String,date: String, time: String, context: Context?, view: View){
        val builder = AlertDialog.Builder(context)
        //set title for alert dialog
        builder.setTitle("Cancel Appointment")
        //set message for alert dialog
        builder.setMessage("Do you want cancel this appointment on $date at $time?")

        //performing positive action
        builder.setPositiveButton("Yes"){dialogInterface, which ->
            cancelAppointment(appointmentId)
            AppointmentList.remove()
            Toast.makeText(context,"Appointment Cancelled",Toast.LENGTH_LONG).show()
            view.findNavController().navigate(ShowAppointmentDetailDirections.actionShowAppointmentDetailToAppointmentHome())
        }
        //performing negative action
        builder.setNegativeButton("No"){dialogInterface, which ->
        }
        // Create the AlertDialog
        val alertDialog: AlertDialog = builder.create()
        // Set other dialog properties
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    // Populates the database with new appointment details
    @RequiresApi(Build.VERSION_CODES.O)
    override fun addNewAppointment(date: String, time: String, userId: String, userName: String, context: Context?, notes : String) : Boolean{
        val appointmentData = AppointmentData(name = userName, date = date, time = time, userId = userId, location = "Office 2", timestamp = buildTimeStamp(), notes = notes)
        db.collection("appointment").add(appointmentData)
            .addOnSuccessListener {
                Toast.makeText(context, "Appointment Booked", Toast.LENGTH_SHORT)
                    .show()
            }
            .addOnFailureListener { ex ->
                Toast.makeText(
                    context, "Host down due to" + ex.message, Toast.LENGTH_LONG
                ).show()
            }
        return true
    }

    override fun showAppointment(user_id: String, function : (Boolean) -> Unit ) {
        db.collection("appointment").whereEqualTo("userId", user_id).get().addOnSuccessListener { documents ->
                for (document in documents) {
                    val data = document.data.get("name")
                    Log.d(TAG, "${data}")
                    var name : String = document.data.get("name").toString()
                    var date : String = document.data.get("date").toString()
                    var time : String = document.data.get("time").toString()
                    var user_id : String = document.data.get("user_id").toString()
                    var location : String = document.data.get("location").toString()
                    var timestamp : String = document.data.get("timestamp").toString()
                    var notes : String = document.data.get("notes").toString()
                    var appointment_id : String = document.id
                    AppointmentList.add(AppointmentData(appointment_id,name, date, time, user_id, location, timestamp, notes))
                    function(true)
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents: ", exception)
                function(false)
            }
    }

    fun getAppointment(user_id: String, date: String, time: String, function: (HashMap<String, String>) -> Unit){
        val currentAppointment : HashMap<String,String> = HashMap()
        db.collection("appointment").whereEqualTo("userId", user_id)
            .whereEqualTo("date",date).whereEqualTo("time", time)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    var name : String = document.data.get("name").toString()
                    var date : String = document.data.get("date").toString()
                    var time : String = document.data.get("time").toString()
                    var user_id : String = document.data.get("user_id").toString()
                    var location : String = document.data.get("location").toString()
                    var timeStamp : String = document.data.get("timestamp").toString()
                    var appointmentId : String = document.id.toString()
                    var notes : String = document.data.get("notes").toString()

                    currentAppointment.put("appointmentId", appointmentId)
                    Log.e(TAG, "$appointmentId")
                    currentAppointment.put("location", location)
                    currentAppointment.put("name", name)
                    currentAppointment.put("notes", notes)
                    Log.e(TAG, "$notes")
                    function(currentAppointment)
                }
            }
            .addOnFailureListener(){
                Log.e(TAG,"${error("Error")}")
            }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun pendingAppointment(user_id : String, function: (Boolean) -> Unit) {
        db.collection("appointment").whereEqualTo("userId", user_id)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val date = document.data.get("date")
                    Log.e(TAG, "$date")
                    if(isPending(date.toString())){
                        var name : String = document.data.get("name").toString()
                        var date : String = document.data.get("date").toString()
                        var time : String = document.data.get("time").toString()
                        var user_id : String = document.data.get("user_id").toString()
                        var location : String = document.data.get("location").toString()
                        var timestamp : String = document.data.get("timestamp").toString()
                        var appointment_id : String = document.id
                        PendingAppointmentList.add(AppointmentData(appointment_id, name, date, time, user_id, location, timestamp))
                        Log.e(TAG, "Pending")
                        function(true)
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents: ", exception)
                function(false)
            }
    }

    override fun cancelAppointment(appointmentId : String){
        db.collection("appointment").document(appointmentId)
            .delete()
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully deleted!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error deleting document", e) }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun isPending(selectedDate : String) : Boolean{
        val currentDate = LocalDate.now()
        val formatterDate = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val formattedDate = currentDate.format(formatterDate)
        if(selectedDate.toString() > formattedDate.toString()){
            return true
        }
        return false
    }

    fun getUserName(uid : String, function: (String) -> Unit){
        db.collection("users").whereEqualTo("user_id", uid)
            .get()
            .addOnSuccessListener{ documents ->
                for(document in documents){
                    val username = document.data.get("name").toString()
                    function(username)
                }
            }
            .addOnFailureListener{
                    e -> Log.w(TAG, "Error fetching username", e)
            }
    }
}