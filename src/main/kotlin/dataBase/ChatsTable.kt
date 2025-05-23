package dataBase

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

object Chats : Table("chats") {
    val chatId = integer("chat_id").autoIncrement()
    val userPhone = varchar("user_phone", 12).references(Users.phoneNumber)
    val contactPhone = varchar("contact_phone", 12).references(Users.phoneNumber)
    val createdAt = text("created_at")

    override val primaryKey = PrimaryKey(chatId)
}


// Функция для проверки и создания чата
// Функция для проверки и создания чата
fun ensureChatExists(userPhone: String, contactPhone: String) {
    transaction {
        // Проверяем, существует ли уже чат между этими пользователями
        val existingChat = Chats.select {
            ((Chats.userPhone eq userPhone) and (Chats.contactPhone eq contactPhone)) or ((Chats.userPhone eq contactPhone) and (Chats.contactPhone eq userPhone))
        }.firstOrNull()

        // Если чат не существует, создаем новый
        if (existingChat == null) {
            val currentDateTime = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)

            Chats.insert {
                it[Chats.userPhone] = userPhone
                it[Chats.contactPhone] = contactPhone
                it[Chats.createdAt] = currentDateTime
            }
        }
    }
}

fun getAllChatsForUser(phoneNumber: String): List<Chat> {
    return transaction {
        Chats.select {
            (Chats.userPhone eq phoneNumber) or (Chats.contactPhone eq phoneNumber)
        }.map {
            Chat(
                chatId = it[Chats.chatId],
                userPhone = it[Chats.userPhone],
                contactPhone = it[Chats.contactPhone],
                createdAt = it[Chats.createdAt]
            )
        }
    }
}