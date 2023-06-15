package hoshino.plugins

import io.ktor.server.application.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    install(CORS) {
        anyHost()
    }
    routing {
        get("/") {
            call.respondText("Hello . Welcome Ai Hoshino !")
        }
    }
}