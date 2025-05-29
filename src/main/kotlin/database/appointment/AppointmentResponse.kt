package com.example.database.appointment

import kotlinx.serialization.Serializable

@Serializable
data class AppointmentResponse(
    val appointmentId: Int,
    val doctorId: String,
    val dateTime: String,
    val doctorFirstName: String? = null,
    val doctorSecondName: String? = null,
    val doctorSpecialization: String? = null
)