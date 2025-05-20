package com.example.database.doctors

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

object Doctors : Table("doctors") {
    val id = varchar("id",50)
    val firstName = varchar("first_name",20)
    val secondName = varchar("second_name",20)
    val thirdName = varchar("third_name",20)
    val spec = varchar("spec",20)
}
