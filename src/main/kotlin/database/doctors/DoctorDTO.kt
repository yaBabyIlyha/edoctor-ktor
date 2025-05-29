package com.example.database.doctors

import kotlinx.serialization.Serializable

@Serializable
class DoctorDTO (
    val id: String,
    val firstName: String,
    val secondName: String,
    val thirdName: String,
    val spec: String
)