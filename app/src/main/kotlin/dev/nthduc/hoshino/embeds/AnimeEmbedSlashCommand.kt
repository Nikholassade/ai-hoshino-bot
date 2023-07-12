package dev.nthduc.hoshino.embeds

import dev.kord.common.Color
import dev.kord.core.event.interaction.ChatInputCommandInteractionCreateEvent
import dev.kord.rest.builder.message.EmbedBuilder
import dev.nthduc.hoshino.utils.getOwnerInfo
import kotlinx.datetime.Clock

class AnimeEmbedSlashCommand(imageUrl: String, event: ChatInputCommandInteractionCreateEvent) {
    private val embed = EmbedBuilder()

    init {
        val (userOwner, avatarOwner) = getOwnerInfo(event)

        embed.author {
            name = "Yêu cầu bởi ${event.interaction.user.username}"
            icon = event.interaction.user.avatar?.cdnUrl?.toUrl()
        }
        embed.image = imageUrl
        embed.color = Color(49,14,76)
        embed.footer {
            text = "Bot được phát triển bởi $userOwner"
            icon = "$avatarOwner"
        }
        embed.timestamp = Clock.System.now()
    }

    fun build(): EmbedBuilder {
        return embed
    }
}
