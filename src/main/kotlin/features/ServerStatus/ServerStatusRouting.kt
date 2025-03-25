package features.ServerStatus

import dataBase.ServerResponse
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.serverStatusRouting() {
    routing {
        get("/serverIsOnline") {
            println("\n==================================")
            println("Запрос статуса: начало")
            call.respond(ServerResponse(true))
            println("Запрос статуса: конец")
            println("==================================\n")
        }
    }
}