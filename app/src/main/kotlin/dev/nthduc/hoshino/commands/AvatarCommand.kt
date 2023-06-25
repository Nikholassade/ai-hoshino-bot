package dev.nthduc.hoshino.commands

import dev.kord.common.Color
import dev.kord.common.entity.Snowflake
import dev.kord.common.entity.optional.firstOrNull
import dev.kord.core.behavior.channel.createEmbed
import dev.kord.core.behavior.interaction.respondPublic
import dev.kord.core.entity.Message
import dev.kord.core.entity.User
import dev.kord.core.entity.interaction.ApplicationCommandInteraction
import dev.kord.core.entity.interaction.Interaction
import dev.kord.core.event.message.MessageCreateEvent
import dev.kord.rest.Image
import dev.kord.rest.builder.message.create.embed
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.datetime.Clock


class AvatarCommand() : Command {
    override suspend fun execute(event: MessageCreateEvent) {
        val message = event.message
        val mentionedUser = message.getUserMention()?.asUser() ?: message.author?.asUser()
        if (mentionedUser != null) {
            val avatarUrl = mentionedUser.avatar?.cdnUrl?.toUrl {
                format = Image.Format.PNG
                size = Image.Size.Size512
            }
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
                timestamp = Clock.System.now()
            }
        }
    }

    suspend fun execute(interaction: Interaction) {
        val commandInteraction = interaction as ApplicationCommandInteraction
        val userOption = commandInteraction.data.data.options.firstOrNull { it.name == "user" }?.value as? String
        val user = if (userOption != null) {
            interaction.kord.getUser(Snowflake(userOption)) ?: return
        } else {
            commandInteraction.user.asUser()
        }
        val avatarUrl = user.avatar?.cdnUrl?.toUrl {
            format = Image.Format.PNG
            size = Image.Size.Size512
        }
        // Check if the user has provided a user ID or @mention.
        if (userOption == null) {
            commandInteraction.respondPublic {
                embed {
                    title = "Please provide a user ID or @mention."
                    color = Color(255, 0, 0)
                }
            }
            return
        }


        // Get the avatar of the user.
        commandInteraction.respondPublic {
            embed {
                title = "Avatar ${user.username}"
                url = avatarUrl
                image = avatarUrl
                footer {
                    text = "Requested by ${commandInteraction.user.username}"
                    icon = commandInteraction.user.avatar?.cdnUrl?.toUrl()
                }
                author {
                    name = user.username
                    icon = avatarUrl
                }
                color = Color(49,14,76)
                timestamp = Clock.System.now()
            }
        }

    }

    private suspend fun Message.getUserMention(): User? {
        return this.mentionedUsers.firstOrNull()
    }

    override val description: String
        get() = "Lấy Avatar"
}