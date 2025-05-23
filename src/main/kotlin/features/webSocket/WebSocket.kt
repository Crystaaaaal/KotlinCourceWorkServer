package features.webSocket




import dataBase.ensureChatExists
import dataBase.getUserByPhoneNumber
import dataBase.saveMessageToDB
import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.time.Duration


fun Application.configureWebSockets() {
    install(WebSockets) {
        pingPeriod = Duration.ofSeconds(15)
        timeout = Duration.ofSeconds(15)
    }

    routing {
        webSocket("/ws/{phoneNumber}") {
            val phoneNumber = call.parameters["phoneNumber"]
                ?: throw IllegalArgumentException("Номер телефона обязателен")

            WebSocketManager.addConnection(phoneNumber, this)

            try {
                for (frame in incoming) {
                    when (frame) {
                        is Frame.Text -> {
                            try {
                                val message = Json.decodeFromString<Message>(frame.readText())
                                println("""
                                    Получено сообщение:
                                    От: ${message.fromUser.phoneNumber} (${message.fromUser.token})
                                    Для: ${message.forUser.phoneNumber} 
                                    Текст: ${message.messageText}
                                    Время: ${message.sentAt}
                                """.trimIndent())

                                val messageRemote = MessageRemote(
                                    forUser = message.forUser.phoneNumber,
                                    fromUser = getUserByPhoneNumber(message.fromUser.phoneNumber)!!,
                                    messageText = message.messageText,
                                    sentAt = message.sentAt)

                                ensureChatExists(message.fromUser.phoneNumber,message.forUser.phoneNumber)
                                saveMessageToDB(message)

                                // Отправка конкретному пользователю
                                WebSocketManager.getConnection(message.forUser.phoneNumber)?.send(
                                    Frame.Text(Json.encodeToString(messageRemote)))
                                //println("текст${messageRemote.messageText} для:${messageRemote.forUser} от: ${messageRemote.fromUser}")
                            } catch (e: Exception) {
                                println("Ошибка парсинга JSON: ${e.message}")
                            }
                        }
                        else -> continue // Игнорируем бинарные фреймы
                    }
                }
            } catch (e: Exception) {
                println("Ошибка соединения: ${e.message}")
            } finally {
                WebSocketManager.removeConnection(phoneNumber = phoneNumber)
                println("Соединение закрыто")
            }
        }
    }
}

//fun Application.configureWebSockets() {
//    install(WebSockets) {
//        pingPeriod = Duration.ofMinutes(1)
//        timeout = Duration.ofSeconds(15)
//        maxFrameSize = Long.MAX_VALUE
//        masking = false
//    }
//
//
//    routing {
//        webSocket("/ws") {
//            try {
//                for (frame in incoming) {
//                    when (frame) {
//                        is Frame.Text -> {
//                            try {
//                                val message = Json.decodeFromString<Message>(frame.readText())
//                                println("""
//                                    Получено сообщение:
//                                    От: ${message.fromUser.phoneNumber} (${message.fromUser.token})
//                                    Для: ${message.forUser.phoneNumber}
//                                    Текст: ${message.messageText}
//                                    Время: ${message.sentAt}
//                                """.trimIndent())
//                            } catch (e: Exception) {
//                                println("Ошибка парсинга JSON: ${e.message}")
//                            }
//                        }
//                        else -> continue // Игнорируем бинарные фреймы
//                    }
//                }
//            } catch (e: Exception) {
//                println("Ошибка соединения: ${e.message}")
//            } finally {
//                println("Соединение закрыто")
//            }
//        }
//    }
//}