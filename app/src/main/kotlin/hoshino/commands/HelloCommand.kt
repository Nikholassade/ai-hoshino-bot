package hoshino.commands

import dev.kord.core.behavior.interaction.respondPublic
import dev.kord.core.entity.interaction.ApplicationCommandInteraction
import dev.kord.core.event.message.MessageCreateEvent
import dev.kord.core.entity.interaction.Interaction

class HelloCommand() : Command {
        override suspend fun execute(event: MessageCreateEvent) {
            val message = event.message
            message.channel.createMessage("Hello, ${message.author?.asUser()?.mention}!")
        }

    suspend fun execute(interaction: Interaction) {
        val commandInteraction = interaction as ApplicationCommandInteraction
        val user = commandInteraction.user.asUser()
        val response = "Hello, ${user.mention}!"
        commandInteraction.respondPublic {
            content = response
        }
    }


    override val description: String
            get() = "Xin chào !"
    }
