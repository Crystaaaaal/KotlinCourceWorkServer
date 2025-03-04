package features.Login

import cache.InMemoryCache
import cache.TokenCache
import features.Registration.RegistrationReceiveRemote
import features.Registration.RegistrationResponseRemote
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.util.UUID

fun Application.configureRegistrationRouting() {
    routing {
        post("/registration") {
            val receive = call.receive<RegistrationReceiveRemote>()
            println("==================================")
            println("Запрос регистрации: старт")
            println(receive.toString())
            if(InMemoryCache.userList.map { it.phoneNumber }.contains(receive.phoneNumber)){
                call.respond(HttpStatusCode.Conflict,"Пользователь уже зарегестрирован")
                println("Пользователь уже зарегистрирован")
                println("Запрос регистрации: конец")
                println("==================================")
                return@post
            }
            println("Пользователь зарегистрирован")

            val token = UUID.randomUUID().toString()
            println("Токен создан")
            InMemoryCache.userList.add(receive)
            InMemoryCache.token.add(
                TokenCache(
                    phoneNumber = receive.phoneNumber,
                    token = token
                )
            )
            call.respond(RegistrationResponseRemote(token=token))
            println("Токен отправлен")
            println("Запрос регистрации: конец")
            println("==================================")
        }
    }
}