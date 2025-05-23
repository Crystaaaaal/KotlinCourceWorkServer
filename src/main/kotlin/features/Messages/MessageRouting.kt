package features.Messages

import dataBase.checkTokenByPhoneNumber
import dataBase.getMessagesBetweenUsers
import features.Searching.PhoneOrLoginRemote
import features.Searching.SearchingRemote
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureMessagesRouting() {
    routing {
        post("/message") {
            println("\n==================================")
            println("Запрос получения сообщений: старт")
            val receive = call.receive<MessagesRecive>()
            println("Данные получены")
            print(receive.toString())
            if (!checkTokenByPhoneNumber(receive.token)){
                call.respond(HttpStatusCode.Conflict, "Неверный токен")
                println("Неверный токен")
                println("Запрос получения сообщений: конец")
                println("==================================\n")
                return@post
            }
            val Remote = getMessagesBetweenUsers(receive.Chat.userPhone, receive.Chat.contactPhone)
            call.respond(Remote)
            println("Список отправлен")
            println("Запрос получения сообщений: конец")
            println("==================================\n")
        }
    }
}