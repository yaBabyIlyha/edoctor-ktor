package com.example.DoctorList

import kotlinx.serialization.Serializable

@Serializable
data class DoctorResponseRemote (
    val id: String,
    val firstName: String,
    val secondName: String,
    val thirdName: String,
    val spec: String,
)