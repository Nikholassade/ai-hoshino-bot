package hoshino.commands

import dev.kord.common.Color
import dev.kord.core.behavior.channel.createEmbed
import dev.kord.core.behavior.interaction.respondPublic
import dev.kord.core.entity.Message
import dev.kord.core.entity.User
import dev.kord.core.entity.interaction.ApplicationCommandInteraction
import dev.kord.core.event.message.MessageCreateEvent
import dev.kord.rest.Image
import dev.kord.core.entity.interaction.Interaction
import dev.kord.rest.builder.message.create.embed
import kotlinx.coroutines.flow.firstOrNull


class AvatarCommand() : Command {
    override suspend fun execute(event: MessageCreateEvent) {
        val message = event.message
        val mentionedUser = message.getUserMention()?.asUser() ?: message.author?.asUser()
        if (mentionedUser != null) {
            val avatarUrl = mentionedUser.avatar?.cdnUrl?.toUrl()
            message.channel.createEmbed {
                title = "Avatar ${mentionedUser.username}"
                url = avatarUrl
                image = avatarUrl
                footer {
                    text = "Requested by ${message.author?.username}"
                    icon = message.author?.avatar?.cdnUrl?.toUrl()
                }
                author {
                    name = mentionedUser.username
                    icon = avatarUrl
                }
                color = Color(49,14,76)
            }
        }
    }

    suspend fun execute(interaction: Interaction) {
        val commandInteraction = interaction as ApplicationCommandInteraction
        val user = commandInteraction.user.asUser()
        val avatarUrl = user.avatar?.cdnUrl?.toUrl()
        commandInteraction.respondPublic {
            embed {
                title = "Avatar ${user.username}"
                url = avatarUrl
                image = avatarUrl
                footer {
                    text = "Requested by ${user.username}"
                    icon = user.avatar?.cdnUrl?.toUrl()
                }
                author {
                    name = user.username
                    icon = avatarUrl
                }
                color = Color(49,14,76)
            }
        }
    }

    private suspend fun Message.getUserMention(): User? {
        return this.mentionedUsers.firstOrNull()
    }

    override val description: String
        get() = "Lấy Avatar"
}