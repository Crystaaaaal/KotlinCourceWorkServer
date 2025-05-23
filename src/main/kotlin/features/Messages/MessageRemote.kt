package features.Messages

import dataBase.Chat
import features.Login.TokenAndPhoneNumberRemote
import features.webSocket.Message
import features.webSocket.MessageRemote
import kotlinx.serialization.Serializable

@Serializable
data class MessagesRecive(
    val Chat: Chat,
    val token: TokenAndPhoneNumberRemote
)

@Serializable
data class MessagesRemote(
    val userMessagesRecive: List<MessageRemote>,
    val contactMessage:List<MessageRemote>,
)