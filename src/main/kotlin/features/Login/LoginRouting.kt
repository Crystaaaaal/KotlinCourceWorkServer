package features.Login

import cache.InMemoryCache
import cache.TokenCache
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.util.UUID

fun Application.configureLoginRouting(){
    routing {
        post("/login"){
            println("==================================")
            println("Запрос авторизации: старт")
            val receive = call.receive<LoginReceiveRemote>()
            println(receive.toString())
            if (InMemoryCache.userList.map{it.phoneNumber}.contains(receive.phoneNumber)){
                val token = UUID.randomUUID().toString()
                println("Токен создан")
                InMemoryCache.token.add(TokenCache(
                    phoneNumber = receive.phoneNumber,
                    token = token
                ))
                call.respond(LoginResponseRemote(token=token))
                println("Токен отправлен")
                println("Вход разрешён")
                println("Запрос авторизации: конец")
                println("==================================")
                return@post
            }
            else{
                call.respond(HttpStatusCode.Conflict,"Пользователь не зарегистрирован")
                println("Неправильные данные")
            }
            println("Запрос авторизации: конец")
            println("==================================")

        }
    }
}