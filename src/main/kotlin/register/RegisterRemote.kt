package com.example.register

import kotlinx.serialization.Serializable

@Serializable
data class RegisterReceiveRemote(
    val login: String,
    val password: String,
    val email: String
)

@Serializable
data class RegisterResponseRemote(
    val token: String
)