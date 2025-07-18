package dataBase

import features.Registration.RegistrationRemote
import hashFunction
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

object Users : Table("users") {
    val phoneNumber = varchar("phone_number", 12) // phoneNumber как первичный ключ
    val hashPassword = varchar("password_hash", 64)
    val fullName = varchar("full_name", 100)
    val login = varchar("login",100)
    val profileImage = binary("profile_image").nullable()
    val createdAt = text("created_at")
    override val primaryKey = PrimaryKey(phoneNumber)
}


fun addUserToDB(NewUser: RegistrationRemote) {
    transaction {
        val sdf = SimpleDateFormat("dd/MM/yyyy hh:mm:ss")
        val currentDate = sdf.format(Date())
        Users.insert {
            it[Users.phoneNumber] = NewUser.phoneNumber
            it[Users.hashPassword] = hashFunction(NewUser.password)
            it[Users.fullName] = NewUser.secondName + " " + NewUser.firstName + " " + NewUser.fatherName
            it[Users.login] = NewUser.login
            it[Users.profileImage] = imageToByteArray()
            it[Users.createdAt] = currentDate
        }
    }
}

fun getHashedPasswordFromDB(phoneNumber: String): String? {
    return transaction {
        Users.select { Users.phoneNumber eq phoneNumber }
            .singleOrNull()
            ?.get(Users.hashPassword) // Получаем хешированный пароль
    }
}
fun getUsersByLogin(login: String): List<User> {
    return transaction {
        Users.select { Users.login like "%$login%" } // Поиск по частичному совпадению
            .map { row ->
                User(
                    phoneNumber = row[Users.phoneNumber],
                    hashPassword = "",
                    fullName = row[Users.fullName],
                    login = row[Users.login],
                    profileImage = row[Users.profileImage],
                    createdAt = row[Users.createdAt]
                )
            }
    }
}

fun getUsersByPhoneNumber(phones: List<String>): List<User> {
    return transaction {
        phones.flatMap { phone ->
            Users.select { Users.phoneNumber eq phone } // Используем точное совпадение вместо like
                .map { row ->
                    User(
                        phoneNumber = row[Users.phoneNumber],
                        hashPassword = "", // Обратите внимание: вы всегда используете пустой пароль
                        fullName = row[Users.fullName],
                        login = row[Users.login],
                        profileImage = row[Users.profileImage],
                        createdAt = row[Users.createdAt]
                    )
                }
        }
    }
}

fun getUserByPhoneNumber(phoneNumber: String): User? {
    return transaction {
        Users.select { Users.phoneNumber eq phoneNumber }
            .singleOrNull()
            ?.let { row ->
                User(
                    phoneNumber = row[Users.phoneNumber],
                    hashPassword = "",
                    fullName = row[Users.fullName],
                    login = row[Users.login],
                    profileImage = row[Users.profileImage],
                    createdAt = row[Users.createdAt]
                )
            }
    }
}

fun getUserByPhoneNumberWithOutImage(phoneNumber: String): User? {
    return transaction {
        Users.select { Users.phoneNumber eq phoneNumber }
            .singleOrNull()
            ?.let { row ->
                User(
                    phoneNumber = row[Users.phoneNumber],
                    hashPassword = "",
                    fullName = row[Users.fullName],
                    login = row[Users.login],
                    profileImage = null,
                    createdAt = row[Users.createdAt]
                )
            }
    }
}

fun checkPassword(phoneNumber: String, inputPassword: String): Boolean {
    val hashedPasswordFromDB = getHashedPasswordFromDB(phoneNumber)
    if (hashedPasswordFromDB == hashFunction(inputPassword)) {
        return true
    }
    return false
}
fun checkUserExistsByPhoneNumber(phoneNumber: String): Boolean {
    return transaction {
        Users.select { Users.phoneNumber eq phoneNumber }.count() > 0
    }
}

fun updateUserProfileImage(phoneNumber: String,newImage: ByteArray):Boolean {
    return transaction {
        if (checkUserExistsByPhoneNumber(phoneNumber)) {
            Users.update({ Users.phoneNumber eq phoneNumber }) {
                it[Users.profileImage] = newImage
            }
            return@transaction true
        }
        false
    }
}



fun imageToByteArray(): ByteArray? {
    return try {
        File("C:\\Users\\HP\\IdeaProjects\\KotlinCourseWorkServer\\src\\main\\kotlin\\dataBase\\userIcon.jpg").readBytes() // Чтение файла в ByteArray
    } catch (e: IOException) {
        e.printStackTrace()
        null
    }
}
