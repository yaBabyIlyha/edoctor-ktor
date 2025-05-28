package com.example

import com.example.DoctorList.configureDoctorsRouting
import com.example.database.users.configureUserRouting
import com.example.login.configureLoginRouting
import com.example.register.configureRegisterRouting
import io.ktor.server.application.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction

fun main() {
    Database.connect(
        url = "jdbc:postgresql://localhost:5432/mydb",
        driver = "org.postgresql.Driver",
        user = "postgres",
        password = "9626"
    ).also {
        transaction {
            addLogger(StdOutSqlLogger)
        }
        println("Database connected successfully")
    }
    embeddedServer(CIO, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    configureSerialization()
    configureRouting()
    configureRegisterRouting()
    configureLoginRouting()
    configureDoctorsRouting()
    configureUserRouting()
}


