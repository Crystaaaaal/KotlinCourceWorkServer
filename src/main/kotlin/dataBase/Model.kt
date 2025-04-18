package dataBase

import kotlinx.serialization.Serializable
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter



@Serializable
data class ServerResponse(
    val success: Boolean
)


@Serializable
data class User(
    val phoneNumber: String,
    val hashPassword:String,
    val fullName: String,
    val login:String,
    val profileImage: ByteArray?,
    val createdAt: String
)

@Serializable
data class Token(
    val id: Int,
    val userId: String,
    val token: String
)

@Serializable
data class Chat(
    val id: Int,
    val createdAt: String
) {
    fun createdAtDateTime(): LocalDateTime {
        return LocalDateTime.parse(createdAt, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
    }
}

@Serializable
data class Message(
    val id: Int,
    val chatId: Int,
    val senderId: Int,
    val messageText: String,
    val sentAt: String
) {
    fun sentAtDateTime(): LocalDateTime {
        return LocalDateTime.parse(sentAt, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
    }
}

@Serializable
data class UserRegistrationRequest(
    val phoneNumber: String,
    val passwordHash: String,
    val fullName: String,
    val profileImage: ByteArray?
)

@Serializable
data class ChatCreationRequest(
    val createdAt: String
)