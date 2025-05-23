package features.Login

import dataBase.User
import kotlinx.serialization.Serializable

@Serializable
data class LoginReceiveRemote(
    val phoneNumber: String,
    val password: String
)

@Serializable
data class TokenAndPhoneNumberRemote(
    val phoneNumber: String,
    val token: String
)

@Serializable
data class TokenAndUserRemote(
    val user: User,
    val token: String
)
