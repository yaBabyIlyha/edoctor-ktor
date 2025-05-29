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
    val senderLogin = varchar("sender_login", 50)
    val receiverLogin = varchar("receiver_login", 50)
    val content = text("content")
    val timestamp = datetime("timestamp")

    override val primaryKey = PrimaryKey(id)

    fun insertMessage(sender: String, receiver: String, message: String) {
        transaction {
            insert {
                it[senderLogin] = sender
                it[receiverLogin] = receiver
                it[content] = message
                it[timestamp] = LocalDateTime.now()
            }
        }
    }


    fun getMessages(sender: String, doctorId: String): List<MessageDTO> {
        return transaction {
            select {
                (Messages.senderLogin eq sender and (Messages.receiverLogin eq doctorId)) or
                        (Messages.senderLogin eq doctorId and (Messages.receiverLogin eq sender))
            }.orderBy(Messages.timestamp to SortOrder.ASC).map {
                MessageDTO(
                    senderLogin = it[senderLogin],
                    receiverLogin = it[receiverLogin],
                    content = it[content],
                    timestamp = it[timestamp].toString()
                )
            }
        }
    }

}
