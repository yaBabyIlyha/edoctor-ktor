import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class MessageDTO(
    val senderLogin: String,
    val receiverLogin: String,
    val content: String,
    val timestamp: String? = null // если хочешь
)
