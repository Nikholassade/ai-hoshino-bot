package hoshino.commands

import dev.kord.core.event.message.MessageCreateEvent
import dev.schlaubi.lavakord.LavaKord
import dev.schlaubi.lavakord.audio.Link
class StopCommand(private val lavalink: LavaKord) : Command {
    override suspend fun execute(event: MessageCreateEvent) {
        val link = lavalink.getLink(event.guildId?.toString() ?: return)
        if (link.state == Link.State.CONNECTED) {
            link.player.stopTrack()
            link.destroy()
            event.message.channel.createMessage("Đã dừng phát nhạc ! Hẹn gặp lại ")
        } else {
            event.message.channel.createMessage("Not currently playing a track")
        }
    }

    override val description: String
        get() = "Stop playing track"
}