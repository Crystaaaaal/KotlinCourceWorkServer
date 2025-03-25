package features.UserInfo

import dataBase.ServerResponse
import dataBase.checkTokenByPhoneNumber
import dataBase.getUserByPhoneNumber
import dataBase.updateUserProfileImage
import features.Login.TokenAndPhoneRemote
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.UpdateUserInfoRouting() {
    routing {
        patch("/updateUserInfo") {
            println("\n==================================")
            println("Запрос обновления фото: начало")
            val receive = call.receive<UpdateUser>()

            if (!checkTokenByPhoneNumber(receive.loginRecive)) {
                call.respond(HttpStatusCode.Conflict, "Неверный токен")
                println("Неверный токен")
                println("Запрос обновления фото: конец")
                println("==================================\n")
                return@patch
            }
            if (updateUserProfileImage(receive.loginRecive.phoneNumber, receive.activeUser.userImage)) {
                call.respond(ServerResponse(true))
                println("Фото обновленно")
                println("Запрос обновления фото: конец")
                println("==================================\n")
                return@patch
            }
            call.respond(ServerResponse(false))
            println("Фото не обновленно")
            println("Запрос обновления фото: конец")
            println("==================================\n")
        }
    }
}