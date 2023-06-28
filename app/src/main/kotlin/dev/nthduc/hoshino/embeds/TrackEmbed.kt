package dev.nthduc.hoshino.embeds

import dev.kord.common.Color
import dev.kord.core.event.message.MessageCreateEvent
import dev.kord.rest.builder.message.EmbedBuilder
import dev.schlaubi.lavakord.rest.models.PartialTrack
import kotlinx.datetime.Clock
import java.time.Duration

class TrackEmbed(track: PartialTrack, event: MessageCreateEvent) {
    private val embed = EmbedBuilder()

    init {
        val duration = Duration.ofMillis(track.info.length)
        val durationText = "%02d:%02d".format(duration.toMinutes(), duration.seconds % 60)
        val videoId = track.info.uri.split("=").last()
        val thumbnailUrl = "https://img.youtube.com/vi/$videoId/maxresdefault.jpg"

        embed.title = "Đang phát"
        embed.description = track.info.title
        embed.thumbnail {
            url = thumbnailUrl
        }
        embed.field {
            name = "Kênh"
            value = track.info.author
            inline = true
        }
        embed.field {
            name = "Thời lượng"
            value = durationText
            inline = true
        }

        embed.field {
            name = "Nguồn"
            value = track.info.sourceName
            inline = true
        }
        embed.color = Color(49, 14, 76)
        embed.timestamp = Clock.System.now()
        embed.footer {
            text = "Developer by ${event.message.author?.username}"
            icon = event.message.author?.avatar?.cdnUrl?.toUrl()
        }
    }

    fun build(): EmbedBuilder {
        return embed
    }
}