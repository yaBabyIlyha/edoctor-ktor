package com.example.database.messages

import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.or
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

@kotlinx.serialization.Serializable
data class ChatItem(
    val userLogin: String,
    val lastMessage: String
)

fun getDoctorChats(doctorId: String): List<ChatItem> {
    return transaction {
        // Берём все сообщения, где участвует доктор как отправитель или получатель
        val messages = Messages.select {
            (Messages.senderLogin eq doctorId) or (Messages.receiverLogin eq doctorId)
        }.orderBy(Messages.timestamp, SortOrder.DESC).toList()

        // Создаём Map, где ключ — userLogin (противоположная сторона), значение — последнее сообщение
        val chatMap = mutableMapOf<String, ChatItem>()

        for (msg in messages) {
            val sender = msg[Messages.senderLogin]
            val receiver = msg[Messages.receiverLogin]
            val userLogin = if (sender == doctorId) receiver else sender
            if (userLogin !in chatMap) {
                chatMap[userLogin] = ChatItem(
                    userLogin = userLogin,
                    lastMessage = msg[Messages.content]
                )
            }
        }

        chatMap.values.toList()
    }
}


fun Route.chatRoutes() {
    route("/api/chats") {
        get("/doctor/{doctorId}") {
            val doctorId = call.parameters["doctorId"] ?: return@get call.respondText(
                "Missing doctorId", status = HttpStatusCode.BadRequest
            )

            val chats = getDoctorChats(doctorId)
            call.respond(chats)
        }
    }
}
