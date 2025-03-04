package dataBase

import features.Registration.RegistrationReceiveRemote
import hashFunction
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

object Users : Table("users") {
    val phoneNumber = varchar("phone_number", 12) // phoneNumber как первичный ключ
    val hashPassword = varchar("password_hash", 64)
    val fullName = varchar("full_name", 100)
    val profileImage = binary("profile_image").nullable()
    val createdAt = text("created_at")

    override val primaryKey = PrimaryKey(phoneNumber)
}

@Serializable
data class User(
    val phoneNumber: String,
    val hashPassword:String,
    val fullName: String,
    val profileImage: ByteArray?,
    val createdAt: String
)

fun addUserToDB(NewUser: RegistrationReceiveRemote) {
    transaction {
        val sdf = SimpleDateFormat("dd/MM/yyyy hh:mm:ss")
        val currentDate = sdf.format(Date())
        Users.insert {
            it[Users.phoneNumber] = NewUser.phoneNumber
            it[Users.hashPassword] = hashFunction(NewUser.password)
            it[Users.fullName] = NewUser.secondName + " " + NewUser.firstName + " " + NewUser.firstName
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
fun getUserByPhoneNumber(phoneNumber: String): User? {
    return transaction {
        Users.select { Users.phoneNumber eq phoneNumber }
            .singleOrNull()
            ?.let { row ->
                User(
                    phoneNumber = row[Users.phoneNumber],
                    hashPassword = row[Users.hashPassword],
                    fullName = row[Users.fullName],
                    profileImage = row[Users.profileImage],
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
fun checkUserExistsByPhoneNumber(User: RegistrationReceiveRemote): Boolean {
    return transaction {
        Users.select { Users.phoneNumber eq User.phoneNumber }.count() > 0
    }
}

fun deliteUserByPhoneNumber(phoneNumber: String): Boolean {
    return transaction {
        val deletedRows = Users.deleteWhere { Users.phoneNumber eq phoneNumber }
        deletedRows > 0
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