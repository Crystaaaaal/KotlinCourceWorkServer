package features.UserInfo

import features.Login.TokenAndPhoneRemote
import kotlinx.serialization.Serializable



@Serializable
data class UpdateUser(
    val loginRecive: TokenAndPhoneRemote,
    val activeUser:ActiveUser
)
@Serializable
data class ActiveUser(
    val userImage: ByteArray,
    val fullName:String)
