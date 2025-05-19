package com.example.login

import com.example.database.tokens.TokenDTO
import com.example.database.tokens.Tokens
import com.example.database.users.UserDTO
import com.example.database.users.Users
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

class LoginController (private val call: ApplicationCall) {

    suspend fun performLogin() {
        val receive = try {
            call.receive<LoginReceiveRemote>().also {
                println("Login attempt for: ${it.login}")
            }
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, "Invalid request format")
            return
        }

        val userDTO = try {
            Users.fetchUser(receive.login).also {
                println("User found: ${it != null}")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            call.respond(HttpStatusCode.InternalServerError, "Database error")
            return
        }

        if (userDTO == null) {
            call.respond(HttpStatusCode.NotFound, "User not found")
            return
        }

        if (userDTO.password != receive.password) {
            call.respond(HttpStatusCode.Unauthorized, "Invalid password")
            return
        }

        try {
            val token = UUID.randomUUID().toString()
            transaction {
                Tokens.insert(
                    TokenDTO(
                        rowId = UUID.randomUUID().toString(),
                        login = receive.login,
                        token = token
                    )
                )
            }
            call.respond(LoginResponseRemote(token = token))
        } catch (e: Exception) {
            e.printStackTrace()
            call.respond(HttpStatusCode.InternalServerError, "Token generation failed: ${e.message}")
        }
    }
}