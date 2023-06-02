package hoshino.commands

import dev.kord.core.event.message.MessageCreateEvent
import java.util.*

class CoinflipCommand : Command {
    override suspend fun execute(event: MessageCreateEvent) {
        val message = event.message
        val random = Random()
        val result = if (random.nextBoolean()) "heads" else "tails"
        message.channel.createMessage("The coin landed on $result!")
    }
}