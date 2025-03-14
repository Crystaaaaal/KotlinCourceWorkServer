package features.Registration

import kotlinx.serialization.Serializable


@Serializable
data class RegistrationRemote(
    val phoneNumber: String,
    val login: String,
    val password: String,
    val secondName: String,
    val firstName:String,
    val fatherName:String
)

@Serializable
data class RegistrationResponseRemote(
    val token: String
)
