package hoshino

import dev.kord.core.Kord
import dev.kord.core.event.message.MessageCreateEvent
import dev.kord.core.on
import hoshino.commands.CoinflipCommand
import hoshino.commands.HelloCommand
import hoshino.handlers.CommandHandler
import io.github.cdimascio.dotenv.dotenv
import org.slf4j.LoggerFactory

suspend fun main() {
    val dotenv = dotenv()
    val token = dotenv["BOT_TOKEN"]
    val client = Kord(token)

    val logger = LoggerFactory.getLogger("hoshino")
    logger.info("Ai Hoshino started")

    val commandHandler = CommandHandler()
    commandHandler.registerCommand("hello", HelloCommand())
    commandHandler.registerCommand("coinflip", CoinflipCommand())
    // Đăng ký các lệnh khác với commandHandler

    client.on<MessageCreateEvent> {
        logger.debug("Received message: ${message.content}")
        commandHandler.handleCommand(this)
    }

    client.login()
}

