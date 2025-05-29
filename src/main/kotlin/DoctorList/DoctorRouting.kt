import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import com.example.database.doctors.Doctors

@Serializable
data class DoctorLoginRequest(
    val id: String,
    val password: String
)

fun Application.configureDoctorsRouting() {
    routing {
        post("/doctor/login") {
            val loginRequest = call.receive<DoctorLoginRequest>()
            val doctor = transaction {
                Doctors.select {
                    (Doctors.id eq loginRequest.id) and (Doctors.password eq loginRequest.password)
                }.singleOrNull()
            }

            if (doctor != null) {
                call.respond(HttpStatusCode.OK)
            } else {
                call.respond(HttpStatusCode.Unauthorized, "Invalid ID or password")
            }
        }
    }
}
