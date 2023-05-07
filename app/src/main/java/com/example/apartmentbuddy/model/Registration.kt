package com.example.apartmentbuddy.model

data class Registration(
    val name: String,
    val email: String,
    val apartmentNum: Number,
    val contactNum: String,
    val password: String,
    val role: String) {
}