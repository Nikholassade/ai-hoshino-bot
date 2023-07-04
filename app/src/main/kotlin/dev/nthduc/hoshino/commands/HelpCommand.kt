package dev.nthduc.hoshino.commands

import dev.kord.common.Color
import dev.kord.core.behavior.channel.createEmbed
import dev.kord.core.behavior.interaction.respondPublic
import dev.kord.core.entity.interaction.ApplicationCommandInteraction
import dev.kord.core.entity.interaction.Interaction
import dev.kord.core.event.message.MessageCreateEvent
import dev.kord.rest.Image
import dev.kord.rest.builder.message.create.embed
import kotlinx.datetime.Clock

class HelpCommand(private val commands: Map<String, Command>) : Command {

    override suspend fun execute(event: MessageCreateEvent) {
        val message = event.message
        val kord = event.kord
        val self = kord.getSelf()
        val avatarUrl = self.avatar?.cdnUrl?.toUrl {
            format = Image.Format.PNG
            size = Image.Size.Size512
        } ?: ""
        message.channel.createEmbed {
            title = "Các lệnh có sẵn"
            description = "Đây là danh sách tất cả các lệnh có sẵn cho bot."
            for ((name, command) in commands) {
                field {
                    this.name = "!$name"
                    value = command.description
                }
            }
            color = Color(49,14,76)
            thumbnail {
                url = avatarUrl
            }
            footer {
                text = "Requested by ${message.author?.username}"
                icon = message.author?.avatar?.cdnUrl?.toUrl()
            }
            timestamp = Clock.System.now()
        }
    }

    suspend fun execute(interaction: Interaction) {
        val commandInteraction = interaction as ApplicationCommandInteraction
        val user = commandInteraction.user.asUser()
        val kord = interaction.kord
        val self = kord.getSelf()
        val avatarUrl = self.avatar?.cdnUrl?.toUrl {
            format = Image.Format.PNG
            size = Image.Size.Size512
        } ?: ""
        commandInteraction.respondPublic {
            embed {
                title = "Các lệnh có sẵn"
                for ((name, command) in commands) {
                    field {
                        this.name = "!$name"
                        value = command.description
                    }
                }
                description = "Đây là danh sách tất cả các lệnh có sẵn cho bot."
                color = Color(49,14,76)
                thumbnail {
                    url = avatarUrl
                }
                footer {
                    text = "Requested by ${user.username}"
                    icon = user.avatar?.cdnUrl?.toUrl()
                }
                timestamp = Clock.System.now()
            }
        }
    }

    override val description: String
        get() = "Displays a list of available commands."
}