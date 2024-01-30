package api

import api.config.DatabaseFactory
import api.dao.CustomerDao
import api.dao.CustomerDaoImpl
import api.plugins.*
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    val dao : CustomerDao = CustomerDaoImpl()
    DatabaseFactory.init()

    configureSerialization()
    configureHTTP()
    configureRouting(dao)
}
