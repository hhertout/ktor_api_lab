package api.plugins

import api.dao.CustomerDao
import api.routes.customerRouting
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting(dao: CustomerDao) {
    routing {
        customerRouting(dao)
    }
}
