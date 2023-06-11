package hoshino.commands.music

import dev.kord.core.event.message.MessageCreateEvent
import dev.schlaubi.lavakord.LavaKord
import dev.schlaubi.lavakord.audio.Link
import hoshino.commands.Command

class ResumeCommand(private val lavalink: LavaKord) : Command {
    override suspend fun execute(event: MessageCreateEvent) {
        val link = lavalink.getLink(event.guildId?.toString() ?: return)
        if (link.state == Link.State.CONNECTED) {
            link.player.unPause()
            event.message.channel.createMessage("Đã tiếp tục phát nhạc !")
        } else {
            event.message.channel.createMessage("Not currently playing a track")
        }
    }

    override val description: String
        get() = "Resume playing track"
}