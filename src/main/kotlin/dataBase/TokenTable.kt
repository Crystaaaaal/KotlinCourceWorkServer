package dataBase

import features.Login.TokenAndPhoneRemote
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

object Tokens : IdTable<Int>("tokens") {
    override val id = integer("id").autoIncrement().entityId() // Уникальный идентификатор токена
    val userId = varchar("user_id", 15).references(
        Users.phoneNumber,
        onDelete = ReferenceOption.CASCADE
    ) // Связь с пользователем
    val token = varchar("token",36) // Сам токен (если нужно хранить)

    override val primaryKey = PrimaryKey(id)
}



fun checkLinkUserToTokenByPhoneNumber(phoneNumber: String): Boolean {
    return transaction {
        Tokens.select { Tokens.userId eq phoneNumber }.count() > 0
    }
}

fun checkTokenByPhoneNumber(token: TokenAndPhoneRemote): Boolean {
    return transaction {
        // Выполняем запрос к таблице Tokens
        Tokens.select {
            (Tokens.userId eq token.phoneNumber) and (Tokens.token eq token.token)
        }.count() > 0 // Если количество записей больше 0, возвращаем true
    }
}




fun addLinkUserToToken(user: User, token: String) {
    transaction {
        if (checkLinkUserToTokenByPhoneNumber(user.phoneNumber)) {
            Tokens.update({ Tokens.userId eq user.phoneNumber }) {
                it[Tokens.token] = token
            }
        } else {
            Tokens.insert {
                it[userId] = user.phoneNumber
                it[Tokens.token] = token
            }
        }
    }
}
