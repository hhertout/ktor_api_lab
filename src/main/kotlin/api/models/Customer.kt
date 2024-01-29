package api.models

import kotlinx.serialization.Serializable

@Serializable
data class Customer(val id: String, val name: String, val email: String)

val customerStorage = mutableListOf<Customer>()