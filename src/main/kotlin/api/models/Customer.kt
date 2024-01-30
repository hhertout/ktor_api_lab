package api.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table

@Serializable
data class Customer(
    val id: Int,
    val name: String,
    val email: String
)

object Customers : Table() {
    val id = integer("id").autoIncrement()
    val name = varchar("name", 128)
    val email = varchar("email", 1024).uniqueIndex()

    override val primaryKey = PrimaryKey(id)
}