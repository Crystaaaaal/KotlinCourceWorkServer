package features.UserInfo

import dataBase.ServerResponse
import dataBase.checkTokenByPhoneNumber
import dataBase.getUserByPhoneNumber
import features.Login.TokenAndPhoneRemote
import features.Searching.PhoneOrLoginRemote
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.SendUserInfoRouting(){
    routing {
        post("/sendUserInfo") {
            println("\n==================================")
            println("Запрос выдачи информации о пользователе: начало")
            val receive = call.receive<TokenAndPhoneRemote>()

            if (!checkTokenByPhoneNumber(receive)) {
                call.respond(HttpStatusCode.Conflict, "Неверный токен")
                println("Неверный токен")
                println("Запрос выдачи информации о пользователе: конец")
                println("==================================\n")
                return@post
            }
            val user = getUserByPhoneNumber(receive.phoneNumber)
            if (user != null) {
                call.respond(ActiveUser(user.profileImage!!, user.fullName))
                println("Данные отправленны")
            }
            println("Запрос выдачи информации о пользователе: конец")
            println("==================================\n")
        }
    }
}