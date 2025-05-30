package com.example.database.appointment

import com.example.database.doctors.Doctors
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
    fun insertAppointment(
        doctorId: String,
        dateTime: LocalDateTime,
        userLogin: String
    ): AppointmentDTO {
        return transaction {
            val insertedId = Appointments.insert {
                it[Appointments.doctorId] = doctorId
                it[Appointments.dateTime] = dateTime
                it[Appointments.userLogin] = userLogin
            } get Appointments.appointmentId

            AppointmentDTO(
                appointmentId = insertedId,
                doctorId = doctorId,
                dateTime = dateTime,
                userLogin = userLogin
            )
        }
    }
    fun getAllAppointmentsByLogin(login: String): List<AppointmentResponse> {
        return transaction {
            Appointments.innerJoin(Doctors, { doctorId }, { Doctors.id })
                .select { Appointments.userLogin eq login }
                .orderBy(Appointments.dateTime to SortOrder.DESC)
                .map { row ->
                    AppointmentResponse(
                        appointmentId = row[Appointments.appointmentId],
                        doctorId = row[Appointments.doctorId],
                        dateTime = row[Appointments.dateTime].toString(),
                        doctorFirstName = row[Doctors.firstName],
                        doctorSecondName = row[Doctors.secondName],
                        doctorSpecialization = row[Doctors.spec]
                    )
                }
        }
    }
}