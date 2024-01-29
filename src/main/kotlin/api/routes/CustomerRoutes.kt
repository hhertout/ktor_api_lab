package api.routes

import api.models.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.customerRouting() {
    route("/customer") {
        get {
            if (customerStorage.isNotEmpty()) {
                call.respond(customerStorage)
            } else {
                call.respondText(
                    "No customer found",
                    status = HttpStatusCode.NoContent,
                    contentType = ContentType.Application.Json
                )
            }
        }

        get("{id?}") {
            val id = call.parameters["id"] ?: return@get call.respondText(
                "No id provided",
                status = HttpStatusCode.BadRequest,
                contentType = ContentType.Application.Json
            )

            val customer = customerStorage.find { it.id == id } ?: return@get call.respondText(
                "Customer not found with id $id",
                status = HttpStatusCode.NotFound,
                contentType = ContentType.Application.Json
            )

            call.respond(customer)
        }

        post {
            val customer = call.receive<Customer>()
            customerStorage.add(customer)

            call.respond(customer)
        }

        delete("{id?}") {
            val id = call.parameters["id"] ?: return@delete call.respond(HttpStatusCode.BadRequest)
            if (customerStorage.removeIf { it.id == id }) {
                call.respondText("Customer removed correctly", status = HttpStatusCode.Accepted)
            } else {
                call.respondText("Not Found", status = HttpStatusCode.NotFound)
            }
        }
    }
}