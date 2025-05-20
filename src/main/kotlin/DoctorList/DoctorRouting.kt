package com.example.DoctorList


import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureDoctorsRouting() {
    routing {
        get ("/doctors") {
            val doctorController = DoctorController(call)
            doctorController.getAllDoctors()
        }
    }
}