package dev.nthduc.hoshino.commands

import dev.kord.core.behavior.interaction.respondPublic
import dev.kord.core.entity.interaction.ApplicationCommandInteraction
import dev.kord.core.entity.interaction.Interaction
import dev.kord.core.event.message.MessageCreateEvent
import java.util.*

@Deprecated("CoinflipCommand Replace CoinflipExtension")
class CoinflipCommand() : Command {
    override suspend fun execute(event: MessageCreateEvent) {
        val message = event.message
        val random = Random()
        val result = if (random.nextBoolean()) "mặt sấp" else "mặt ngửa"
        message.channel.createMessage("Đồng xu rơi vào `$result`!")
    }

    suspend fun execute(interaction: Interaction) {
        val commandInteraction = interaction as ApplicationCommandInteraction
        val random = Random()
        val result = if (random.nextBoolean()) "mặt sấp" else "mặt ngửa"
        val response = "Đồng xu rơi vào `$result`!"
        commandInteraction.respondPublic {
            content = response
        }
    }

    override val description: String
        get() = "Lật xu"
}