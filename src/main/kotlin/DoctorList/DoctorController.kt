package com.example.DoctorList

import com.example.database.doctors.Doctors
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

class DoctorController(private val call: ApplicationCall) {

    suspend fun getAllDoctors() {
        try {
            val doctors = transaction {
                Doctors.selectAll().map {
                    DoctorResponseRemote (
                        id = it[Doctors.id],
                        firstName = it[Doctors.firstName],
                        secondName = it[Doctors.secondName],
                        thirdName = it[Doctors.thirdName],
                        spec = it[Doctors.spec]
                    )
                }
            }
            call.respond(doctors)
        } catch (e: Exception) {
            e.printStackTrace()
            call.respond(HttpStatusCode.BadRequest, "Faild: ${e.message}")
        }
    }
}