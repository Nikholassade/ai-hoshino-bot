package hoshino.commands

import dev.kord.common.Color
import dev.kord.core.behavior.channel.createEmbed
import dev.kord.core.behavior.interaction.respondPublic
import dev.kord.core.entity.interaction.ApplicationCommandInteraction
import dev.kord.core.event.message.MessageCreateEvent
import dev.kord.core.entity.interaction.Interaction
import dev.kord.rest.builder.message.create.embed

class HelpCommand(private val commands: Map<String, Command>) : Command {
    override suspend fun execute(event: MessageCreateEvent) {
        val message = event.message
        message.channel.createEmbed {
            title = "Available Commands"
            for ((name, command) in commands) {
                field {
                    this.name = "!$name"
                    value = command.description
                }
            }
            color = Color(49,14,76)
            footer {
                text = "Requested by ${message.author?.username}"
                icon = message.author?.avatar?.cdnUrl?.toUrl()
            }
        }
    }

    suspend fun execute(interaction: Interaction) {
        val commandInteraction = interaction as ApplicationCommandInteraction
        val user = commandInteraction.user.asUser()
        commandInteraction.respondPublic {
            embed {
                title = "Available Commands"
                for ((name, command) in commands) {
                    field {
                        this.name = "!$name"
                        value = command.description
                    }
                }
                color = Color(49,14,76)
                footer {
                    text = "Requested by ${user.username}"
                    icon = user.avatar?.cdnUrl?.toUrl()
                }
            }
        }
    }

    override val description: String
        get() = "Displays a list of available commands."
}