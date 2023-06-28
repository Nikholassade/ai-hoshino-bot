package dev.nthduc.hoshino.commands.music

import dev.kord.common.entity.ButtonStyle
import dev.kord.core.event.message.MessageCreateEvent
import dev.kord.rest.builder.component.ActionRowBuilder
import dev.nthduc.hoshino.embeds.TrackEmbed
import dev.schlaubi.lavakord.audio.Link
import dev.schlaubi.lavakord.rest.models.PartialTrack

class TrackPlayer(private val link: Link) {
    val nextTrack = queue.removeFirstOrNull()
    suspend fun playNextTrack(queue: MutableList<PartialTrack>, event: MessageCreateEvent) {

        if (nextTrack != null) {
            try {
                link.player.playTrack(nextTrack)
            } catch (e: Exception) {
                println("Error playing track: ${e.message}")
            }
            val embed = TrackEmbed(nextTrack, event).build()

            event.kord.rest.channel.createMessage(event.message.channelId) {
                embeds.add(embed)
                components.add(ActionRowBuilder().apply {
                    interactionButton(ButtonStyle.Primary, "pauseBtn") {
                        label = "Tạm dừng phát"
                        disabled = false
                    }
                    interactionButton(ButtonStyle.Secondary, "resumeBtn") {
                        label = "Tiếp tục phát"
                        disabled = true
                    }
                    interactionButton(ButtonStyle.Success, "skipBtn") {
                        label = "Bài tiếp theo"
                        disabled = queue.size < 1
                    }
                })
            }
        }
    }
}

