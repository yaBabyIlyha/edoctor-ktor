package com.example.register

import com.example.database.tokens.TokenDTO
import com.example.database.tokens.Tokens
import com.example.database.users.UserDTO
import com.example.database.users.Users
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

class RegisterController(val call: ApplicationCall) {

    suspend fun registerNewUser() {
        val registerReceiveRemote = call.receive<RegisterReceiveRemote>()
        val userDTO = Users.fetchUser(registerReceiveRemote.login)

        if (userDTO != null) {
            call.respond(HttpStatusCode.BadRequest, "User already exists")
        } else {
            val token = UUID.randomUUID().toString()
            try {
                transaction {
                    Users.insert(
                        UserDTO(
                            login = registerReceiveRemote.login,
                            password = registerReceiveRemote.password,
                            email = registerReceiveRemote.email,
                            value = ""
                        )
                    )
                    Tokens.insert(
                        TokenDTO(
                            rowId = UUID.randomUUID().toString(),
                            login = registerReceiveRemote.login,
                            token = token
                        )
                    )
                }
                call.respond(RegisterResponseRemote(token = token))
            } catch (e: ExposedSQLException) {
                call.respond(HttpStatusCode.BadRequest, "Something went wrong! :(  : ${e.message}")
            }
        }
    }
}