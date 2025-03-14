package features.Searching

import dataBase.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.SearchingRouting() {
    routing {
        post("/search") {
            println("\n==================================")
            println("Запрос поиска: старт")

            val receive = call.receive<PhoneOrLoginRemote>()
            println("Данные получены")
            print(receive.toString())
            if (!checkTokenByPhoneNumber(receive.token)){
                call.respond(HttpStatusCode.Conflict, "Неверный токен")
                println("Неверный токен")
                println("Запрос поиска: конец")
                println("==================================\n")
                return@post
            }
            val userList = getUsers(receive.phoneOrLogin)

            call.respond(SearchingRemote(userList))
            println("Список отправлен")
            println("Запрос поиска: конец")
            println("==================================\n")
        }
    }
}
fun getUsers(numberOrLogin: String): List<User> {
    val userList: MutableList<User> = mutableListOf() // Используем MutableList для добавления элементов
    val startsWithPlus7 = numberOrLogin.startsWith("+7")

    if (startsWithPlus7) {
        val user = getUserByPhoneNumber(numberOrLogin) // Получаем пользователя по номеру телефона
        if (user != null) {
            userList.add(user) // Добавляем пользователя в список, если он найден
        }
        println("Поиск по номеру телефона:")
        for (user in userList) {
            println(user)
        }
    } else {
        val users = getUsersByLogin(numberOrLogin) // Получаем список пользователей по логину
        userList.addAll(users) // Добавляем всех найденных пользователей в список
        println("Поиск по логину:")
        for (user in userList) {
            println(user)
        }
    }

    return userList
}
