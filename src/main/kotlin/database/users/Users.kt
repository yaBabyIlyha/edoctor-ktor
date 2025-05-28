package com.example.database.users

import org.jetbrains.exposed.sql.Except
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

object Users : Table("users") {
    val login = varchar("login", 30)
    val password = varchar("password", 28)
    val email = varchar("email", 30)
    val value = varchar("value", 10)

    fun insert(userDTO : UserDTO) {
        transaction {
            insert {
                it[login] = userDTO.login
                it[password] = userDTO.password
                it[email] = userDTO.email ?: ""
                it[value] = userDTO.value ?: ""
            }
        }
    }

    fun fetchUser(login: String): UserDTO? {
        return try {
            transaction {
                Users.selectAll().where { Users.login eq login.trim() }.singleOrNull()?.let {
                    UserDTO(
                        login = it[Users.login],
                        password = it[password],
                        email = it[email],
                        value = it[value]
                    )
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
