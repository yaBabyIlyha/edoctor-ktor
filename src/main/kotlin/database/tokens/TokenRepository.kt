package com.example.database.tokens

import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

class TokenRepository {

    public fun getLoginByToken(token: String): String? {
        return transaction {
            Tokens.selectAll()
                .where { Tokens.token eq token }
                .singleOrNull()
                ?.also { foundToken ->
                    println("Found token in DB: ${foundToken[Tokens.token]}, login: ${foundToken[Tokens.login]}")
                }
                ?.let { it[Tokens.login] }
        }
    }
}