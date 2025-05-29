package com.example.database.users

import com.example.database.appointment.AppointmentResponse
import com.example.database.appointment.Appointments
import com.example.database.doctors.Doctors
import com.example.database.doctors.fetchDoctor
import com.example.database.tokens.AuthTokens.getLoginForToken
import com.example.database.tokens.TokenRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable

fun Application.configureUserRouting() {
    val tokenRepository = TokenRepository()

    routing {
        get("/user") {
            val authHeader = call.request.headers["Authorization"]
            println("Received auth header: $authHeader") // Логируем заголовок

            val token = authHeader?.removePrefix("Bearer ")?.trim()
                ?: throw IllegalArgumentException("Token missing").also {
                    println("Token is missing in headers")
                }

            println("Extracted token: $token") // Логируем извлечённый токен

            val login = tokenRepository.getLoginByToken(token)
                ?: throw IllegalArgumentException("Invalid token: $token").also {
                    println("Token not found in DB: $token")
                }

            println("Found login: $login") // Логируем найденный логин

            val user = Users.fetchUser("Ilya")
                ?: throw IllegalArgumentException("User not found").also {
                    println("User not found for login: Ilya")
                }

            call.respond(UserDataResponse(user.login, user.email ?: "", user.value ?: "0"))
        }
        get("/user/upcoming-appointment") {
            val login = call.request.queryParameters["login"]
                ?: throw BadRequestException("Missing login parameter")

            println("Searching appointment for login: $login") // Логирование

            val appointment = Appointments.getUpcomingAppointment(login)
                ?: throw NotFoundException("No upcoming appointments for user $login")

            val doctor = fetchDoctor(appointment.doctorId)
                ?: throw NotFoundException("Doctor not found with id ${appointment.doctorId}")

            call.respond(
                AppointmentResponse(
                    appointmentId = appointment.appointmentId,
                    doctorId = appointment.doctorId,
                    dateTime = appointment.dateTime.toString(),
                    doctorFirstName = doctor.firstName,
                    doctorSecondName = doctor.secondName
                )
            )
        }
        get("/appointment/next") {
            val token = getTokenFromHeader(call)
                ?: return@get call.respond(HttpStatusCode.Unauthorized, "Missing token")

            val login = getLoginForToken(token)
                ?: return@get call.respond(HttpStatusCode.Unauthorized, "Invalid token")

            val appointment = Appointments.getUpcomingAppointment(login)
                ?: return@get call.respond(HttpStatusCode.NotFound, "No appointment")

            val doctor = fetchDoctor(appointment.doctorId)
                ?: return@get call.respond(HttpStatusCode.NotFound, "Doctor not found")

            call.respond(
                AppointmentResponse(
                    appointmentId = appointment.appointmentId,
                    doctorId = appointment.doctorId,
                    dateTime = appointment.dateTime.toString(),
                    doctorFirstName = doctor.firstName,
                    doctorSecondName = doctor.secondName,
                    doctorSpecialization = doctor.spec
                )
            )
        }

    }
}

fun getTokenFromHeader(call: ApplicationCall): String? {
    val authHeader = call.request.headers["Authorization"] ?: return null
    return if (authHeader.startsWith("Bearer ")) {
        authHeader.removePrefix("Bearer ").trim()
    } else null
}


@Serializable
data class UserDataResponse (
    val login: String,
    val email: String,
    val value: String
)