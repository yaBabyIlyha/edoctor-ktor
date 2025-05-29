package com.example.DoctorList

import kotlinx.serialization.Serializable

@Serializable
data class DoctorLoginRequest(
    val id: String,
    val password: String
)