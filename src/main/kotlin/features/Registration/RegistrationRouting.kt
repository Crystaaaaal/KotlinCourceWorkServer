package features.Login

import dataBase.ServerResponse
import dataBase.addUserToDB
import dataBase.checkUserExistsByPhoneNumber
import features.Registration.RegistrationRemote
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRegistrationRouting() {
    routing {
        post("/registration") {
            println("\n==================================")
            val receive = call.receive<RegistrationRemote>()
            println("Запрос регистрации: старт")
            println(receive.toString())
            if(checkUserExistsByPhoneNumber(receive)){
                call.respond(HttpStatusCode.Conflict,"Пользователь уже зарегестрирован")
                println("Пользователь уже зарегистрирован \nЗапрос регистрации: конец")
                println("==================================\n")
                return@post
            }
            println("Пользователь зарегистрирован")
            addUserToDB(receive)
            call.respond(ServerResponse(true))
            println("Запрос регистрации: конец")
            println("==================================\n")
        }
    }
}