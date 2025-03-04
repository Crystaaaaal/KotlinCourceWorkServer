package dataBase

import features.Registration.RegistrationReceiveRemote
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

fun AddUserToDB(NewUser: RegistrationReceiveRemote){
    Users.insert {
        it[Users.phoneNumber] = NewUser.phoneNumber
        it[Users.passwordHash] = NewUser.password
        it[Users.fullName] = NewUser.secondName +" " + NewUser.firstName + " " + NewUser.firstName
        it[Users.profileImage] = imageToByteArray()
        val sdf = SimpleDateFormat("dd/MM/yyyy hh:mm:ss")
        val currentDate = sdf.format(Date())
        it[Users.createdAt] = currentDate
    }
}

fun isUserExistsByPhoneNumber(User: RegistrationReceiveRemote): Boolean {
    return Users.select { Users.phoneNumber eq User.phoneNumber }.count() > 0
}
fun imageToByteArray(): ByteArray? {
    return try {
        File("C:\\Users\\HP\\IdeaProjects\\KotlinCourseWorkServer\\src\\main\\kotlin\\dataBase\\userIcon.jpg").readBytes() // Чтение файла в ByteArray
    } catch (e: IOException) {
        e.printStackTrace()
        null
    }
}
//fun Route.userRoutes() {
//    route("/users") {
//        // Регистрация пользователя
//        post {
//            val userData = call.receive<UserRegistrationRequest>()
//            val userId = transaction {
//                Users.insertAndGetId {
//                    it[phoneNumber] = userData.phoneNumber
//                    it[passwordHash] = userData.passwordHash
//                    it[fullName] = userData.fullName
//                    it[profileImage] = userData.profileImage
//                    it[createdAt] = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
//                }.value
//            }
//            call.respond(HttpStatusCode.Created, mapOf("userId" to userId))
//        }
//
//        // Получение всех пользователей
//        get {
//            val users = transaction {
//                Users.selectAll().map {
//                    User(
//                        id = it[Users.id].value, // Получаем значение ID
//                        phoneNumber = it[Users.phoneNumber],
//                        fullName = it[Users.fullName],
//                        profileImage = it[Users.profileImage],
//                        createdAt = it[Users.createdAt]
//                    )
//                }
//            }
//            call.respond(users)
//        }
//    }
//}
//
//fun Route.chatRoutes() {
//    route("/chats") {
//        // Создание чата
//        post {
//            val chatData = call.receive<ChatCreationRequest>()
//            val chatId = transaction {
//                Chats.insertAndGetId {
//                    it[createdAt] = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
//                }.value
//            }
//            call.respond(HttpStatusCode.Created, mapOf("chatId" to chatId))
//        }
//    }
//}