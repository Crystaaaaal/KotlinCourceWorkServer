package dataBase

import features.Messages.MessagesRemote
import features.webSocket.Message
import features.webSocket.MessageRemote
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDateTime

object Messages : Table("messages") {
    val messageId = integer("message_id").autoIncrement()
    val chatId = integer("chat_id").references(Chats.chatId)
    val senderPhone = varchar("sender_phone", 12).references(Users.phoneNumber)
    val text = text("text")
    val timestamp = varchar("timestamp", 26)

    override val primaryKey = PrimaryKey(messageId)
}

fun saveMessageToDB(message: Message) {
    transaction {
        // Получаем chatId для чата между пользователями
        val chat = Chats.select {
            ((Chats.userPhone eq message.forUser.phoneNumber) and (Chats.contactPhone eq message.fromUser.phoneNumber)) or
                    ((Chats.userPhone eq message.fromUser.phoneNumber) and (Chats.contactPhone eq message.forUser.phoneNumber))
        }.firstOrNull()

        val chatId = chat?.get(Chats.chatId)

        if (chatId != null) {
            // Сохраняем сообщение в таблицу Messages
            Messages.insert {
                it[Messages.chatId] = chatId
                it[Messages.senderPhone] = message.fromUser.phoneNumber
                it[Messages.text] = message.messageText
                it[Messages.timestamp] = message.sentAt
            }
        } else {
            throw IllegalStateException("Chat between users does not exist.")
        }
    }
}

fun getMessagesBetweenUsers(phone1: String, phone2: String): MessagesRemote {
    return transaction {
        // Получаем chatId для чата между пользователями
        val chatId = Chats.slice(Chats.chatId)
            .select {
                ((Chats.userPhone eq phone1) and (Chats.contactPhone eq phone2)) or
                        ((Chats.userPhone eq phone2) and (Chats.contactPhone eq phone1))
            }
            .singleOrNull()
            ?.get(Chats.chatId)

        if (chatId != null) {
            // Получаем все сообщения в этом чате
            val messages = Messages.select { Messages.chatId eq chatId }
                .map { row ->
                    val senderPhone = row[Messages.senderPhone]
                    val user = getUserByPhoneNumberWithOutImage(senderPhone) // Используем функцию для получения пользователя
                    MessageRemote(
                        forUser = if (senderPhone == phone1) phone2 else phone1,
                        fromUser = user?.copy(profileImage = null) ?: User(senderPhone, "", "", "", null, ""),
                        messageText = row[Messages.text],
                        sentAt = row[Messages.timestamp]
                    )
                }

            // Разделяем сообщения на два списка в зависимости от отправителя
            val userMessagesRecive = messages.filter { it.fromUser.phoneNumber == phone1 }
            val contactMessage = messages.filter { it.fromUser.phoneNumber == phone2 }

            MessagesRemote(userMessagesRecive, contactMessage)
        } else {
            MessagesRemote(emptyList(), emptyList())
        }
    }
}

