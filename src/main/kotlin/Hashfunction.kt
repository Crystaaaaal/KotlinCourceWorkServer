import java.math.BigInteger
import java.security.MessageDigest

fun hashFunction(password:String):String{
    // Создаём объект MessageDigest для алгоритма SHA-256
    val digest = MessageDigest.getInstance("SHA-256")

    // Преобразуем пароль в массив байтов и хешируем
    val hashBytes = digest.digest(password.toByteArray(Charsets.UTF_8))

    // Преобразуем массив байтов в строку в шестнадцатеричном формате
    return BigInteger(1, hashBytes).toString(16).padStart(64, '0')
}