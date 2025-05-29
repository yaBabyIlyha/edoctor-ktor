package com.example.database.doctors

import com.example.database.users.UserDTO
import com.example.database.users.Users
import com.example.database.users.Users.email
import com.example.database.users.Users.password
import com.example.database.users.Users.value
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import javax.print.Doc

object Doctors : Table("doctors") {
    val id = varchar("id",50)
    val firstName = varchar("first_name",20)
    val secondName = varchar("second_name",20)
    val thirdName = varchar("third_name",20)
    val spec = varchar("spec",20)

    override val primaryKey = PrimaryKey(id)
}

public fun fetchDoctor(id: String): DoctorDTO? {
    return try {
        transaction {
            Doctors.selectAll().where { Doctors.id eq id }.singleOrNull()?.let {
                DoctorDTO(
                    id = it[Doctors.id],
                    spec = it[Doctors.spec],
                    firstName = it[Doctors.firstName],
                    secondName = it[Doctors.secondName],
                    thirdName = it[Doctors.thirdName]
                )
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}
