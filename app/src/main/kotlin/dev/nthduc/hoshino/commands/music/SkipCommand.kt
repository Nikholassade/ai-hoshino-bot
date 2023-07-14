package dev.nthduc.hoshino.commands.music

import dev.kord.core.event.message.MessageCreateEvent
import dev.nthduc.hoshino.commands.Command
import dev.schlaubi.lavakord.LavaKord

@Deprecated("SkipCommand Replace MusicExtension")
class SkipCommand(private val lavalink: LavaKord) : Command {
    override suspend fun execute(event: MessageCreateEvent) {
        val link = lavalink.getLink(event.guildId?.toString() ?: return)
        if (queue.size > 1) {
            val currentTrack = link.player.playingTrack
            if (currentTrack != null) {
                val duration = currentTrack.length.inWholeSeconds
                print("DURATION: $duration")
                link.player.seekTo(duration - currentTrack.length.inWholeSeconds.minus(1))
            }
            event.message.channel.createMessage("Skipped current track")
        } else {
            event.message.channel.createMessage("Không thể bỏ qua bài hát cuối cùng trong danh sách phát")
        }
    }
    override val description: String
        get() = "Bỏ qua bài hát hiện tại "
}