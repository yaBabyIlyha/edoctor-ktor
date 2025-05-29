package com.example.database.messages

import MessageDTO
import com.example.database.appointment.AppointmentDTO
import com.example.database.appointment.Appointments
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.request.receive


fun Route.messageRouting() {
    post("/chat/send") {
        val body = call.receive<MessageDTO>()
        Messages.insertMessage(body.senderLogin, body.receiverLogin, body.content)
        call.respond(HttpStatusCode.OK)
    }

    get("/chat/{login}/{doctorId}") {
        val login = call.parameters["login"]!!
        val doctorId = call.parameters["doctorId"]!!
        call.respond(Messages.getMessages(login, doctorId))
    }

    post("/appointment") {
        val request = call.receive<AppointmentDTO>()
        val appointment = Appointments.insertAppointment(
            doctorId = request.doctorId,
            dateTime = request.dateTime,
            userLogin = request.userLogin
        )
        call.respond(appointment)
    }
}
