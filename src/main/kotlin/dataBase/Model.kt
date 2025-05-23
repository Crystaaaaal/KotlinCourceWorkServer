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
) {
    override fun toString(): String {
        return "phoneNumber: $phoneNumber" +
                "hashPassword: $hashPassword" +
                "fullName: $fullName" +
                "login: $login" +
                "createdAt: $createdAt"
    }
}

@Serializable
data class Token(
    val id: Int,
    val userId: String,
    val token: String
)

@Serializable
data class Chat(
    val chatId: Int,
    val userPhone: String,
    val contactPhone: String,
    val createdAt: String
)



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