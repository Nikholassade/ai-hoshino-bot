package dev.nthduc.hoshino

import com.kotlindiscord.kord.extensions.ExtensibleBot
import dev.kord.common.annotation.KordPreview
import dev.kord.common.entity.Snowflake
import dev.kord.core.Kord
import dev.kord.core.event.message.MessageCreateEvent
import dev.kord.gateway.Intent
import dev.kord.gateway.PrivilegedIntent
import dev.nthduc.hoshino.commands.HelloCommand
import dev.nthduc.hoshino.config.*
import dev.nthduc.hoshino.extensions.*
import dev.nthduc.hoshino.extensions.anime.*
import dev.nthduc.hoshino.extensions.music.MusicExtension
import dev.nthduc.hoshino.handlers.SlashCommandHandler
import dev.nthduc.hoshino.plugins.configureRouting
import dev.schlaubi.lavakord.kord.lavakord
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.slf4j.LoggerFactory


@OptIn(KordPreview::class, PrivilegedIntent::class)
suspend fun main() {
    coroutineScope {
        launch {
            embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
                .start(wait = true)
        }
        val token = BOT_TOKEN
        val applicationId = Snowflake(APPLICATION_ID)
        val client = Kord(token)

        val lavalink = client.lavakord()
        lavalink.addNode(
            serverUri = LAVALINK_SERVER,
            password = LAVALINK_PASSWORD
        )

        val logger = LoggerFactory.getLogger("hoshino")
        logger.info("Ai Hoshino started")

        // Create an instance of the SlashCommandHandler
        val slashCommandHandler = SlashCommandHandler(client, applicationId)
        val helloCommand = HelloCommand()

        slashCommandHandler.registerCommand("hello", "Hello slash command", helloCommand::execute)
        slashCommandHandler.listen()

        val bot = ExtensibleBot(token) {
            chatCommands {
                defaultPrefix = DEFAULT_PREFIX
                enabled = true
                invokeOnMention = true


            }
            extensions {
                add(::AvatarExtension)
                add(::CoinflipExtension)
                add(::ServerInfoExtension)
                add(::UserInfoExtension)
                add(::AboutExtension)
                add(::AiHoshinoExtension)
                add(::CuddleExtension)
                add(::FeedExtension)
                add(::HugExtension)
                add(::KissExtension)
                add(::PatExtension)
                add(::SlapExtension)
                add(::SmugExtension)
                add(::TickleExtension)
                add(::SearchAnimeExtension)
                add(::SearchAnimeSauceNaoExtension)
                add(::MusicExtension)
                add(::HelpExtension)
            }
            presence {
                watching("Oshi no Ko")
            }
            intents {
                +Intent.MessageContent
                +Intent.GuildVoiceStates
                +Intent.GuildMessageTyping
                +Intent.Guilds
                +Intent.AutoModerationConfiguration
                +Intent.AutoModerationExecution
                +Intent.GuildWebhooks
                +Intent.GuildMembers
                +Intent.GuildPresences
            }
        }
        bot.on<MessageCreateEvent>{
            logger.debug("Received message: ${message.content}")
            message.channel.type()
        }
        bot.start()
    }

}

fun Application.module() {
    configureRouting()
}


