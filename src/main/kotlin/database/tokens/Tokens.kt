package com.example.database.tokens

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.select

object Tokens : Table("token") {
    val login = varchar("login", 30)
    val token = varchar("token", 50)
    val id = varchar("id", 50)

    fun insert(tokenDTO: TokenDTO) {
        Tokens.insert {
            it[login] = tokenDTO.login
            it[token] = tokenDTO.token
            it[id] = tokenDTO.rowId
        }
    }
}

object AuthTokens {
    fun getLoginForToken(token: String): String? {
        return transaction {
            Tokens.select { Tokens.token eq token }
                .mapNotNull { it[Tokens.login] }
                .singleOrNull()
        }
    }
}

