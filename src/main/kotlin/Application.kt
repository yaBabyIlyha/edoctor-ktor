package com.example

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
    // Сначала подключаемся к БД
    Database.connect(
        url = "jdbc:postgresql://localhost:5432/mydb",
        driver = "org.postgresql.Driver",
        user = "postgres",
        password = "9626"
    ).also {
        // Добавляем логгер
        transaction {
            addLogger(StdOutSqlLogger)
        }
        println("Database connected successfully")
    }

    transaction {
        // Простой тестовый запрос
        val result = exec("SELECT 1") { rs ->
            rs.next()
            rs.getInt(1)
        }
        println("Database test query result: $result")
    }

    // Затем запускаем сервер
    embeddedServer(CIO, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    configureSerialization()
    configureRouting()
    configureRegisterRouting()
    configureLoginRouting()
}


