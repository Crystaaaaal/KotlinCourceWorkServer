package features.webSocket


import dataBase.User
import features.Login.TokenAndPhoneNumberRemote
import kotlinx.serialization.Serializable

@Serializable
data class Message(
    val forUser: User,
    val fromUser: TokenAndPhoneNumberRemote,
    val messageText: String,
    val sentAt: String
)

@Serializable
data class MessageRemote(
    val forUser: String,
    val fromUser: User,
    val messageText: String,
    val sentAt: String
)