package com.example.apartmentbuddy.model

object ComplainList {

    private val complainList = mutableListOf<Complalistdata>()

    fun add(complain: Complalistdata) {
        if(!complainList.contains(complain)){
            complainList.add(complain)
        }
    }

    fun remove(){
        ComplainList.complainList.clear()
    }


    // Method to fetch the data in Appointment List
    fun getAllComplains(): List<Complalistdata> {
        return complainList

    }
}