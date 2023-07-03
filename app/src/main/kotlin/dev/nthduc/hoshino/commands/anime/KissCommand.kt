package dev.nthduc.hoshino.commands.anime
import dev.kord.common.Color
import dev.kord.common.entity.Snowflake
import dev.kord.core.behavior.channel.createMessage
import dev.kord.core.entity.Message
import dev.kord.core.entity.User
import dev.kord.core.event.message.MessageCreateEvent
import dev.kord.rest.builder.message.create.embed
import dev.kord.x.emoji.Emojis
import dev.nthduc.hoshino.commands.Command
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import kotlinx.serialization.Serializable

val client = HttpClient(CIO) {
    install(ContentNegotiation) {
        json()
    }
}

@Serializable
data class NekosLifeResponse(val url: String)

class KissCommand : Command {
    override suspend fun execute(event: MessageCreateEvent) {
        val message = event.message
        val mentionedUser = message.getUserMention()?.asUser()?.mention ?: message.author?.asUser()?.mention
        if (mentionedUser != null) {
            val response: NekosLifeResponse = client.get("https://nekos.life/api/v2/img/kiss").body()
            val imageUrl = response.url

            message.channel.createMessage {
                content = "SHEEESHHHHH ${Emojis.couplekiss} ${message.author?.mention} đã hôn $mentionedUser say đắm !"
                embed {
                    author {
                        name = "Yêu cầu bởi ${message.author?.username}"
                        icon = message.author?.avatar?.cdnUrl?.toUrl()
                    }
                    image = imageUrl
                    color = Color(49,14,76)
                    footer {
                        text = "Bot được phát triển bởi ${runBlocking { event.kord.getUser(Snowflake(681140407765172232)) }?.username.toString()}"
                        icon = runBlocking { event.kord.getUser(Snowflake(681140407765172232)) }?.avatar?.cdnUrl?.toUrl()
                    }
                    timestamp = Clock.System.now()
                }
            }
        }
    }

    private suspend fun Message.getUserMention(): User? {
        return this.mentionedUsers.firstOrNull()
    }

    override val description: String
        get() = "Hôn một ai đó"
}
