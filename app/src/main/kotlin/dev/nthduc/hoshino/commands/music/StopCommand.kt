package dev.nthduc.hoshino.commands.music

import dev.kord.core.event.message.MessageCreateEvent
import dev.nthduc.hoshino.commands.Command
import dev.schlaubi.lavakord.LavaKord
import dev.schlaubi.lavakord.audio.Link

class StopCommand(private val lavalink: LavaKord) : Command {
    override suspend fun execute(event: MessageCreateEvent) {
        val link = lavalink.getLink(event.guildId?.toString() ?: return)
        if (link.state == Link.State.CONNECTED) {
            link.disconnectAudio()
            queue.clear()
            link.destroy()
            event.message.channel.createMessage("Đã dừng phát nhạc ! Hẹn gặp lại ❌ ")
        } else {
            event.message.channel.createMessage("❌ ***Hiện tại không phát bài hát nào***")
        }
    }

    override val description: String
        get() = "Dừng phát nhạc"
}