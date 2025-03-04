package features.Login

import kotlinx.serialization.Serializable

@Serializable
data class LoginReceiveRemote(
    val phoneNumber: String,
    val password: String
)

@Serializable
data class LoginResponseRemote(
    val token: String
)
