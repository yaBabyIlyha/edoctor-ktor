package com.example.database.appointment

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.greaterEq
import org.jetbrains.exposed.sql.javatime.datetime
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDateTime

object Appointments : Table("appointment") {
    val appointmentId = integer("appointment_id").autoIncrement()
    val doctorId = varchar("doctor_id", 10)
    val dateTime = datetime("date_time")
    val userLogin = varchar("user_login", 20)

    override val primaryKey = PrimaryKey(appointmentId)

    fun getUpcomingAppointment(login: String): AppointmentDTO? {
        return transaction {
            selectAll()
                .where {
                    (userLogin eq login) and (dateTime greaterEq LocalDateTime.now())
                }
                .orderBy(dateTime to SortOrder.ASC)
                .limit(1)
                .singleOrNull()
                ?.let { row ->
                    AppointmentDTO(
                        appointmentId = row[appointmentId],
                        doctorId = row[doctorId],
                        dateTime = row[dateTime],
                        userLogin = row[userLogin],
                    )
                }
        }
    }
}