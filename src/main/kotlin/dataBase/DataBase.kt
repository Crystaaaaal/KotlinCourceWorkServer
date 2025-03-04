package dataBase

import dataBase.Chats.autoIncrement
import dataBase.Chats.entityId
import dataBase.Messages.autoIncrement
import dataBase.Messages.entityId
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table
import java.text.SimpleDateFormat
import java.util.*





object Chats : IdTable<Int>("chats") { // Указываем тип Int для IdTable
    override val id = integer("id").autoIncrement().entityId() // Используем entityId()
    val createdAt = text("created_at")

    override val primaryKey = PrimaryKey(id)
}

object ChatParticipants : Table("chat_participants") {
    val chatId = integer("chat_id").references(Chats.id)
    val userId = varchar("user_id",15).references(Users.phoneNumber)

    override val primaryKey = PrimaryKey(chatId, userId)
}

object Messages : IdTable<Int>("messages") { // Указываем тип Int для IdTable
    override val id = integer("id").autoIncrement().entityId() // Используем entityId()
    val chatId = integer("chat_id").references(Chats.id)
    val senderId = varchar("sender_id",15).references(Users.phoneNumber)
    val messageText = text("message_text")
    val sentAt = text("sent_at")

    override val primaryKey = PrimaryKey(id)
}