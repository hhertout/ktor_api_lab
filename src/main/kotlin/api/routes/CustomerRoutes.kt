package api.routes

import api.dao.CustomerDao
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.postgresql.util.PSQLException
import java.lang.Exception

fun Route.customerRouting(dao: CustomerDao) {
    @Serializable
    data class Message(val message: String)

    @Serializable
    data class NewCustomer(val email: String, val name: String)

    @Serializable
    data class UpdateCustomer(val id: Int, val email: String? = null, val name: String? = null)

    route("/customer") {
        get {
            try {
                val customers = dao.getCustomers()

                if (customers.isEmpty()) {
                    call.respondText(
                        Json.encodeToString(Message("No customers found")),
                        status = HttpStatusCode.NoContent,
                        contentType = ContentType.Application.Json
                    )
                } else {
                    call.respond(customers)
                }
            } catch (err: Exception) {
                call.respondText(
                    Json.encodeToString(Message("An error occurred")),
                    contentType = ContentType.Application.Json,
                    status = HttpStatusCode.InternalServerError
                )
            }
        }

        post {
            val customer = call.receive<NewCustomer>()
            try {
                dao.addCustomer(customer.name, customer.email)
                call.respond(customer)
            } catch (err: PSQLException) {
                call.respondText(
                    Json.encodeToString(Message("An error occurred during register")),
                    contentType = ContentType.Application.Json,
                    status = HttpStatusCode.InternalServerError,
                )
            } catch (err: Exception) {
                call.respondText(
                    Json.encodeToString(Message("An error occurred")),
                    contentType = ContentType.Application.Json,
                    status = HttpStatusCode.InternalServerError
                )
            }
        }

        get("{id?}") {
            val id = call.parameters["id"] ?: return@get call.respondText(
                Json.encodeToString(Message("No id provided")),
                status = HttpStatusCode.BadRequest,
                contentType = ContentType.Application.Json,
            )

            val result = dao.findCustomerById(id.toInt())
            result?.let {
                call.respond(it)
            } ?: return@get call.respondText(
                Json.encodeToString(Message("Customer not found")),
                status = HttpStatusCode.BadRequest,
                contentType = ContentType.Application.Json,
            )
        }

        patch {
            val updateData = call.receive<UpdateCustomer>()
            try {
                val customerFound = dao.findCustomerById(updateData.id) ?: return@patch call.respondText(
                    Json.encodeToString(Message("Customer doesn't exist")),
                    status = HttpStatusCode.BadRequest,
                    contentType = ContentType.Application.Json,
                )

                val res = dao.updateCustomer(
                    updateData.id,
                    updateData.name ?: customerFound.name,
                    updateData.email ?: customerFound.email
                )

                if (res) {
                    call.respond(updateData)
                } else {
                    call.respondText(
                        Json.encodeToString(Message("An error occurred")),
                        contentType = ContentType.Application.Json,
                        status = HttpStatusCode.InternalServerError
                    )
                }
            } catch (err: Exception) {
                call.respondText(
                    Json.encodeToString(Message("An error occurred")),
                    contentType = ContentType.Application.Json,
                    status = HttpStatusCode.InternalServerError
                )
            }
        }
        delete("{id?}") {
            val id = call.parameters["id"] ?: return@delete call.respondText(
                Json.encodeToString(Message("No id provided")),
                status = HttpStatusCode.BadRequest,
                contentType = ContentType.Application.Json,
            )

            try {
                val res = dao.deleteCustomer(id.toInt())
                if (res) {
                    call.respondText(
                        Json.encodeToString(Message("Customer deleted successfully")),
                        contentType = ContentType.Application.Json,
                    )
                } else {
                    call.respondText(
                        Json.encodeToString(Message("An error occurred")),
                        contentType = ContentType.Application.Json,
                        status = HttpStatusCode.InternalServerError
                    )
                }
            } catch (err: Exception) {
                call.respondText(
                    Json.encodeToString(Message("An error occurred")),
                    contentType = ContentType.Application.Json,
                    status = HttpStatusCode.InternalServerError
                )
            }
        }
    }
}