package features.Searching

import dataBase.User
import features.Login.LoginReceiveRemote
import features.Login.TokenAndPhoneNumberRemote
import kotlinx.serialization.Serializable

@Serializable
data class SearchingRemote(
    val userList: List<User>
)

@Serializable
data class PhoneOrLoginRemote(
    val phoneOrLogin: String,
    val token: TokenAndPhoneNumberRemote
)