package features.Quit

import dataBase.*
import features.Chats.ChatRemote
import features.Login.TokenAndPhoneNumberRemote
import features.Messages.MessagesRecive
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlin.io.println

fun Application.configureQuitRouting() {
    routing {
        post("/Quit") {
            println("\n==================================")
            println("Запрос выхода: старт")
            val receive = call.receive<TokenAndPhoneNumberRemote>()
            println("Данные получены")
            print(receive.toString())
            if (!checkTokenByPhoneNumber(receive)){
                call.respond(HttpStatusCode.Conflict, "Неверный токен")
                println("Неверный токен")
                println("Запрос выхода: конец")
                println("==================================\n")
                return@post
            }
            deleteTokenByPhoneNumber(receive.phoneNumber)
            println("Токен удалён")
            println("Запрос выхода: конец")
            println("==================================\n")
        }
    }
}