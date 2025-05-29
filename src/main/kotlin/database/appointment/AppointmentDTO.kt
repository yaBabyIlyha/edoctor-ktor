package com.example.database.appointment

import LocalDateTimeSerializer
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class AppointmentDTO (
    val appointmentId: Int = 0,  // <-- default значение
    val doctorId: String,
    @Serializable(with = LocalDateTimeSerializer::class)
    val dateTime: LocalDateTime,
    val userLogin: String
)