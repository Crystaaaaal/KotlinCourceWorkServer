package cache

import features.Registration.RegistrationReceiveRemote

data class TokenCache(
    val phoneNumber: String,
    val token: String
)

object InMemoryCache{
    val userList: MutableList<RegistrationReceiveRemote> = mutableListOf()
    val token: MutableList<TokenCache> = mutableListOf()
}