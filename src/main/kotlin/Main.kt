import dataBase.*
import features.Searching.SearchingRouting
import features.Login.configureLoginRouting
import features.Login.configureRegistrationRouting
import features.ServerStatus.serverStatusRouting
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
        SchemaUtils.create(Users, Tokens,Chats, ChatParticipants, Messages)
    }

    // Запуск сервера
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {

        configureSerialization()
        configureRegistrationRouting()
        configureLoginRouting()
        serverStatusRouting()
        SearchingRouting()



//        routing {
//            userRoutes()
//            chatRoutes()
//        }
    }.start(wait = true)
}