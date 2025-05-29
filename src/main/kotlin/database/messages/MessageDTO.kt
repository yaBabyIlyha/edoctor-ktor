import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class MessageDTO(
    val sender: String,
    val content: String,
    val receiverDoctorId: String
)
