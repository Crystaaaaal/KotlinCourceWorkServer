package features.Chats

import dataBase.checkTokenByPhoneNumber
import dataBase.getAllChatsForUser
import dataBase.getMessagesBetweenUsers
import dataBase.getUsersByPhoneNumber
import features.Login.TokenAndPhoneNumberRemote
import features.Messages.MessagesRecive
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureChatsRouting() {
    routing {
        post("/Chats") {
            println("\n==================================")
            println("Запрос получения чатов: старт")
            val receive = call.receive<TokenAndPhoneNumberRemote>()
            println("Данные получены")
            print(receive.toString())
            if (!checkTokenByPhoneNumber(receive)){
                call.respond(HttpStatusCode.Conflict, "Неверный токен")
                println("Неверный токен")
                println("Запрос получения чатов: конец")
                println("==================================\n")
                return@post
            }
            val chats = getAllChatsForUser(receive.phoneNumber)
            println("Chats" + chats.toString())
            val phones = chats.map { chat ->
                if (chat.userPhone == receive.phoneNumber) {
                    chat.contactPhone  // берем contactPhone, если userPhone равен response.phoneNumber
                } else {
                    chat.userPhone     // иначе берем userPhone (значит contactPhone равен response.phoneNumber)
                }
            }.distinct() // убираем дубликаты на случай одинаковых чатов
            println("phones" + phones.toString())
            val users = getUsersByPhoneNumber(phones)
            val Remote = ChatRemote(chats, users)
            call.respond(Remote)
            println(getAllChatsForUser(receive.phoneNumber))
            println("Список отправлен")
            println("Запрос получения чатов: конец")
            println("==================================\n")
        }
    }
}