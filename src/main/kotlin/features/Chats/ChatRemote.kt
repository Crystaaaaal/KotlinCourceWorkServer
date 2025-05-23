package features.Chats

import dataBase.Chat
import dataBase.User
import features.webSocket.MessageRemote
import kotlinx.serialization.Serializable

@Serializable
data class ChatRemote(
    val userChatsRecive: List<Chat>,
    val Users: List<User>
)