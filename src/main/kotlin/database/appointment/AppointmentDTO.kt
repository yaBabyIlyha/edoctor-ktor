package com.example.database.appointment

import java.time.LocalDateTime

data class AppointmentDTO (
    val appointmentId: Int,
    val doctorId: String,
    val dateTime: LocalDateTime,
    val userLogin: String
)