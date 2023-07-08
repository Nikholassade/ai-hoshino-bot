package dev.nthduc.hoshino.commands

import dev.kord.common.Color
import dev.kord.core.behavior.channel.createEmbed
import dev.kord.core.entity.Message
import dev.kord.core.entity.User
import dev.kord.core.event.message.MessageCreateEvent
import dev.kord.rest.Image
import dev.nthduc.hoshino.utils.getOwnerInfo
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.datetime.Clock


class AvatarCommand() : Command {
    override suspend fun execute(event: MessageCreateEvent) {
        val message = event.message
        val (userOwner, avatarOwner) = getOwnerInfo(event)
        val mentionedUser = message.getUserMention()?.asUser() ?: message.author?.asUser()
        if (mentionedUser != null) {
            val avatarUrl = mentionedUser.avatar?.cdnUrl?.toUrl {
                format = Image.Format.PNG
                size = Image.Size.Size512
            }
            message.channel.createEmbed {
                title = "Avatar ${mentionedUser.username}"
                description = "[JPEG](${mentionedUser.avatar?.cdnUrl?.toUrl{
                    format = Image.Format.JPEG
                    size = Image.Size.Size512
                }}) | [PNG](${mentionedUser.avatar?.cdnUrl?.toUrl{
                    format = Image.Format.PNG
                    size = Image.Size.Size512
                }}) | [WEBP](${mentionedUser.avatar?.cdnUrl?.toUrl{
                    format = Image.Format.WEBP
                    size = Image.Size.Size512
                }}) | [LOTTIE](${mentionedUser.avatar?.cdnUrl?.toUrl{
                    format = Image.Format.LOTTIE
                    size = Image.Size.Size512
                }})"
                image = mentionedUser.avatar?.cdnUrl?.toUrl{
                    format = Image.Format.PNG
                    size = Image.Size.Size512
                }
                url = avatarUrl
                image = avatarUrl
                footer {
                    text = "Bot được phát triển bởi $userOwner"
                    icon = "$avatarOwner"
                }
                author {
                    name = "${message.author?.username}"
                    icon = message.author?.avatar?.cdnUrl?.toUrl()
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