package hoshino

import dev.kord.common.annotation.KordPreview
import dev.kord.common.entity.Snowflake
import dev.kord.core.Kord
import dev.kord.core.event.message.MessageCreateEvent
import dev.kord.core.on
import dev.kord.gateway.Intent
import dev.kord.gateway.PrivilegedIntent
import dev.schlaubi.lavakord.kord.lavakord
import hoshino.commands.*
import hoshino.handlers.CommandHandler
import hoshino.handlers.SlashCommandHandler
import io.github.cdimascio.dotenv.dotenv
import org.slf4j.LoggerFactory

@OptIn(KordPreview::class)
suspend fun main() {
    val dotenv = dotenv()
    val token = dotenv["BOT_TOKEN"]
    val applicationId = Snowflake(dotenv["APPLICATION_ID"])
    val client = Kord(token)

    val lavalink = client.lavakord()
    lavalink.addNode(
        serverUri = dotenv["LAVALINK_SERVER"],
        password =  dotenv["LAVALINK_PASSWORD"]
    )

    val logger = LoggerFactory.getLogger("hoshino")
    logger.info("Ai Hoshino started")

    val commandHandler = CommandHandler()
    commandHandler.registerCommand("hello", HelloCommand())
    commandHandler.registerCommand("coinflip", CoinflipCommand())
    commandHandler.registerCommand("avatar", AvatarCommand())
    commandHandler.registerCommand("help", HelpCommand(commandHandler.commands))
    commandHandler.registerCommand("play", PlayCommand(lavalink,client))
    commandHandler.registerCommand("stop", StopCommand(lavalink))
    commandHandler.registerCommand("pause", PauseCommand(lavalink))

    // Create an instance of the SlashCommandHandler
    val slashCommandHandler = SlashCommandHandler(client, applicationId)
    val helloCommand = HelloCommand()
    val coinflipCommand = CoinflipCommand()
    val avatarCommand = AvatarCommand()
    val helpCommand = HelpCommand(commandHandler.commands)
    slashCommandHandler.registerCommand("hello", "Hello slash command", helloCommand::execute)
    slashCommandHandler.registerCommand("coinflip", "Flip a coin", coinflipCommand::execute)
    slashCommandHandler.registerCommand("avatar", "Get Avatar", avatarCommand::execute)
    slashCommandHandler.registerCommand("help", "Help", helpCommand::execute)


    slashCommandHandler.listen()
    // Đăng ký các lệnh khác với commandHandler

    client.on<MessageCreateEvent> {
        logger.debug("Received message: ${message.content}")
        commandHandler.handleCommand(this)
    }

    client.login{
        presence { playing("I am Ai Hoshino") }

        @OptIn(PrivilegedIntent::class)
        intents += Intent.MessageContent
        intents += Intent.GuildVoiceStates
        intents += Intent.GuildMessageTyping
        intents += Intent.Guilds
        intents += Intent.AutoModerationConfiguration
        intents += Intent.AutoModerationExecution
        intents += Intent.AutoModerationExecution
        intents += Intent.DirectMessageTyping
        intents += Intent.DirectMessages
        intents += Intent.GuildWebhooks

    }
}

