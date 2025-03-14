package features.Searching

import dataBase.User
import features.Login.TokenAndPhoneRemote
import kotlinx.serialization.Serializable

@Serializable
data class SearchingRemote(
    val userList: List<User>
)

@Serializable
data class PhoneOrLoginRemote(
    val phoneOrLogin: String,
    val token: TokenAndPhoneRemote
)