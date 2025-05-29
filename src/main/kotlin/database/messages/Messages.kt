package com.example.database.messages

import MessageDTO
import kotlinx.coroutines.selects.select
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.javatime.datetime
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDateTime

object Messages : Table("messages") {
    val id = integer("id").autoIncrement()
    val senderLogin = varchar("sender_login", 30)
    val receiverDoctorId = varchar("receiver_doctor_id", 10)
    val content = text("content")
    val timestamp = datetime("timestamp")

    override val primaryKey = PrimaryKey(id)

    fun insertMessage(sender: String, doctorId: String, message: String) {
        transaction {
            insert {
                it[senderLogin] = sender
                it[receiverDoctorId] = doctorId
                it[content] = message
            }
        }
    }

    fun getMessages(sender: String, doctorId: String): List<MessageDTO> {
        return transaction {
            select {
                (Messages.senderLogin eq sender and (Messages.receiverDoctorId eq doctorId)) or
                        (Messages.senderLogin eq doctorId and (Messages.receiverDoctorId eq sender))
            }.orderBy(timestamp to SortOrder.ASC).map {
                MessageDTO(
                    sender = it[senderLogin],
                    content = it[content],
                    receiverDoctorId = it[receiverDoctorId]
                )
            }
        }
    }
}
