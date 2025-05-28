package com.example.database.users

import com.example.database.tokens.TokenRepository
import io.ktor.server.application.*
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
    }
}

@Serializable
data class UserDataResponse (
    val login: String,
    val email: String,
    val value: String
)