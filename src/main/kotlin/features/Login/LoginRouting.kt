package features.Login
import dataBase.addLinkUserToToken
import dataBase.checkPassword
import dataBase.getUserByPhoneNumber
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.util.UUID

fun Application.configureLoginRouting() {
    routing {
        post("/login") {
            println("\n==================================")
            println("Запрос авторизации: старт")
            val receive = call.receive<LoginReceiveRemote>()
            println(receive.toString())
            val user = getUserByPhoneNumber(receive.phoneNumber)
            if (user != null) {
                println()
                if (!checkPassword(user.phoneNumber, receive.password)) {
                    call.respond(HttpStatusCode.Conflict, "Неверный пароль")
                    println("Неверный пароль")
                    println("Запрос авторизации: конец")
                    println("==================================\n")
                    return@post
                }
                val token = UUID.randomUUID().toString()
                println("Токен создан")
                addLinkUserToToken(user, token)
                call.respond(LoginResponseRemote(token = token))
                println("Токен отправлен")
                println("Вход разрешён")
                println("Запрос авторизации: конец")
                println("==================================\n")
                return@post
            }
        else{
        call.respond(HttpStatusCode.Conflict, "Пользователь не зарегистрирован")
        println("Неправильные данные")
    }
        println("Запрос авторизации: конец")
        println("==================================\n")

    }
}
}