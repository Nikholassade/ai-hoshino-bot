package hoshino

import dev.kord.common.annotation.KordPreview
import dev.kord.common.entity.Snowflake
import dev.kord.core.Kord
import dev.kord.core.behavior.interaction.respondPublic
import dev.kord.core.entity.interaction.ApplicationCommandInteraction
import dev.kord.core.event.message.MessageCreateEvent
import dev.kord.core.on
import dev.kord.gateway.Intent
import dev.kord.gateway.PrivilegedIntent
import hoshino.commands.AvatarCommand
import hoshino.commands.CoinflipCommand
import hoshino.commands.HelloCommand
import hoshino.commands.HelpCommand
import hoshino.handlers.CommandHandler
import hoshino.handlers.SlashCommandHandler
import io.github.cdimascio.dotenv.dotenv
import kotlinx.serialization.json.JsonNull.content
import org.slf4j.LoggerFactory

@OptIn(KordPreview::class)
suspend fun main() {
    val dotenv = dotenv()
    val token = dotenv["BOT_TOKEN"]
    val applicationId = Snowflake(dotenv["APPLICATION_ID"])
    val client = Kord(token)

    val logger = LoggerFactory.getLogger("hoshino")
    logger.info("Ai Hoshino started")

    val commandHandler = CommandHandler()
    commandHandler.registerCommand("hello", HelloCommand())
    commandHandler.registerCommand("coinflip", CoinflipCommand())
    commandHandler.registerCommand("avatar", AvatarCommand())
    commandHandler.registerCommand("help", HelpCommand(commandHandler.commands))

    // Create an instance of the SlashCommandHandler
    val slashCommandHandler = SlashCommandHandler(client, applicationId)
    val helloCommand = HelloCommand()
    slashCommandHandler.registerCommand("hello", "Hello slash command", helloCommand::execute)
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
    }
}

