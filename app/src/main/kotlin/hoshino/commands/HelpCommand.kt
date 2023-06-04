package hoshino.commands

import dev.kord.common.Color
import dev.kord.core.behavior.channel.createEmbed
import dev.kord.core.event.message.MessageCreateEvent

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

    override val description: String
        get() = "Displays a list of available commands."
}