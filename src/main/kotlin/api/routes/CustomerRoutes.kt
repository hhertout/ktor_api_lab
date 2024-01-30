package api.routes

import api.dao.CustomerDao
import api.models.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable

fun Route.customerRouting(dao: CustomerDao) {
    @Serializable
    data class NewCustomer(val email: String, val name: String)

    route("/customer") {
        get {
            val customers = dao.getCustomers()

            if (customers.isEmpty()) {
                call.respondText(
                    "No customer found",
                    status = HttpStatusCode.NoContent,
                    contentType = ContentType.Application.Json
                )
            } else {
                call.respond(customers)
            }
        }

        post {

            val customer = call.receive<NewCustomer>()
            dao.addCustomer(customer.name, customer.email)

            call.respond(customer)
        }
    }
}