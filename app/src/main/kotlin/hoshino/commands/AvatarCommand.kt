package hoshino.commands

import dev.kord.common.Color
import dev.kord.core.behavior.channel.createEmbed
import dev.kord.core.entity.Message
import dev.kord.core.entity.User
import dev.kord.core.event.message.MessageCreateEvent
import kotlinx.coroutines.flow.firstOrNull

class AvatarCommand : Command {
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

    private suspend fun Message.getUserMention(): User? {
        return this.mentionedUsers.firstOrNull()
    }
}