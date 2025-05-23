package features.UserInfo

import features.Login.TokenAndPhoneNumberRemote
import kotlinx.serialization.Serializable



@Serializable
data class UpdateUser(
    val loginRecive: TokenAndPhoneNumberRemote,
    val activeUser:ActiveUser
)
@Serializable
data class ActiveUser(
    val userImage: ByteArray,
    val fullName:String)
