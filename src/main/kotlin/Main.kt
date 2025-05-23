import dataBase.*
import features.Chats.configureChatsRouting
import features.Searching.SearchingRouting
import features.Login.configureLoginRouting
import features.Login.configureRegistrationRouting
import features.Messages.configureMessagesRouting
import features.Quit.configureQuitRouting
import features.ServerStatus.serverStatusRouting
import features.UserInfo.SendUserInfoRouting
import features.UserInfo.UpdateUserInfoRouting
import features.webSocket.configureWebSockets
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

fun main() {
    // Подключение к SQLite
    Database.connect("jdbc:sqlite:messenger.db", driver = "org.sqlite.JDBC")

    // Создание таблиц
    transaction {
        SchemaUtils.create(Users, Tokens,Messages,Chats)
    }

    // Запуск сервера
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {

        configureSerialization()
        configureRegistrationRouting()
        configureLoginRouting()
        serverStatusRouting()
        SearchingRouting()
        SendUserInfoRouting()
        UpdateUserInfoRouting()
        configureWebSockets()
        configureMessagesRouting()
        configureChatsRouting()
        configureQuitRouting()

    }.start(wait = true)
}