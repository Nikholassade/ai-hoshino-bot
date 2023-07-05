package dev.nthduc.hoshino.utils

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.Serializable

val client = HttpClient(CIO) {
    install(ContentNegotiation) {
        json()
    }
}

@Serializable
data class NekosLifeResponse(val url: String)