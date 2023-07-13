package dev.nthduc.hoshino

import com.kotlindiscord.kord.extensions.ExtensibleBot
import dev.kord.common.annotation.KordPreview
import dev.kord.common.entity.Snowflake
import dev.kord.core.Kord
import dev.kord.core.event.message.MessageCreateEvent
import dev.kord.core.on
import dev.kord.gateway.Intent
import dev.kord.gateway.PrivilegedIntent
import dev.nthduc.hoshino.commands.HelloCommand
import dev.nthduc.hoshino.commands.HelpCommand
import dev.nthduc.hoshino.commands.music.*
import dev.nthduc.hoshino.config.*
import dev.nthduc.hoshino.extensions.*
import dev.nthduc.hoshino.extensions.anime.*
import dev.nthduc.hoshino.handlers.CommandHandler
import dev.nthduc.hoshino.handlers.SlashCommandHandler
import dev.nthduc.hoshino.plugins.configureRouting
import dev.schlaubi.lavakord.kord.lavakord
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.slf4j.LoggerFactory


@OptIn(KordPreview::class)
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

        val commandHandler = CommandHandler()
        commandHandler.registerCommand("hello", HelloCommand())
        commandHandler.registerCommand("help", HelpCommand(commandHandler.commands))
        commandHandler.registerCommand("play", PlayCommand(lavalink, client))

        commandHandler.registerCommand("stop", StopCommand(lavalink))
        commandHandler.registerCommand("pause", PauseCommand(lavalink))
        commandHandler.registerCommand("resume", ResumeCommand(lavalink))
        commandHandler.registerCommand("leave", LeaveCommand(lavalink))
        commandHandler.registerCommand("connect", ConnectCommand(lavalink))
        commandHandler.registerCommand("skip", SkipCommand(lavalink))
        commandHandler.registerCommand("nowplaying", NowPlayingCommand(lavalink))
        commandHandler.registerCommand("lyrics", LyricsCommand(lavalink))

        // Create an instance of the SlashCommandHandler
        val slashCommandHandler = SlashCommandHandler(client, applicationId)
        val helloCommand = HelloCommand()
        val helpCommand = HelpCommand(commandHandler.commands)

        slashCommandHandler.registerCommand("hello", "Hello slash command", helloCommand::execute)
        slashCommandHandler.registerCommand("help", "Help", helpCommand::execute)
        slashCommandHandler.listen()


        client.on<MessageCreateEvent> {
            logger.debug("Received message: ${message.content}")
            message.channel.type()
            commandHandler.handleCommand(this)
        }
        val bot = ExtensibleBot(token) {
            chatCommands {
                defaultPrefix = DEFAULT_PREFIX
                enabled = true
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
            }
            presence {
                watching("Oshi no Ko")
            }
        }
        bot.start()

        client.login {
            presence { watching("Oshi no Ko") }


            @OptIn(PrivilegedIntent::class)
            intents += Intent.MessageContent
            intents += Intent.GuildVoiceStates
            intents += Intent.GuildMessageTyping
            intents += Intent.Guilds
            intents += Intent.GuildWebhooks
            intents += Intent.AutoModerationConfiguration
            intents += Intent.AutoModerationExecution
            intents += Intent.AutoModerationExecution
            intents += Intent.DirectMessageTyping
            intents += Intent.DirectMessages
            intents += Intent.GuildWebhooks
        }

    }

}

fun Application.module() {
    configureRouting()
}


