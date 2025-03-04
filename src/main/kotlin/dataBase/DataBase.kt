package dataBase

import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Table
import java.text.SimpleDateFormat
import java.util.*


object Users : IdTable<Int>("users") { // Указываем тип Int для IdTable
    override val id = integer("id").autoIncrement().entityId() // Используем entityId()
    val phoneNumber = varchar("phone_number", 15).uniqueIndex()
    val passwordHash = varchar("password_hash", 255)
    val fullName = varchar("full_name", 100)
    val profileImage = binary("profile_image").nullable()
    val createdAt = text("created_at")

    override val primaryKey = PrimaryKey(id)
}

object Chats : IdTable<Int>("chats") { // Указываем тип Int для IdTable
    override val id = integer("id").autoIncrement().entityId() // Используем entityId()
    val createdAt = text("created_at")

    override val primaryKey = PrimaryKey(id)
}

object ChatParticipants : Table("chat_participants") {
    val chatId = integer("chat_id").references(Chats.id)
    val userId = integer("user_id").references(Users.id)

    override val primaryKey = PrimaryKey(chatId, userId)
}

object Messages : IdTable<Int>("messages") { // Указываем тип Int для IdTable
    override val id = integer("id").autoIncrement().entityId() // Используем entityId()
    val chatId = integer("chat_id").references(Chats.id)
    val senderId = integer("sender_id").references(Users.id)
    val messageText = text("message_text")
    val sentAt = text("sent_at")

    override val primaryKey = PrimaryKey(id)
}