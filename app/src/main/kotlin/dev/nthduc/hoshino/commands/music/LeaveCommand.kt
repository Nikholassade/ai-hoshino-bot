package dev.nthduc.hoshino.commands.music

import dev.kord.core.event.message.MessageCreateEvent
import dev.nthduc.hoshino.commands.Command
import dev.schlaubi.lavakord.LavaKord
import dev.schlaubi.lavakord.audio.Link

class LeaveCommand(private val lavalink: LavaKord) : Command {
    override suspend fun execute(event: MessageCreateEvent) {
        val link = lavalink.getLink(event.guildId?.toString() ?: return)
        if (link.state == Link.State.CONNECTED) {
            if (queue.size > 0) {
                queue.clear()
            }
            link.disconnectAudio()
            link.destroy()
            event.message.channel.createMessage("Đã rời khỏi kênh thoại ! Hẹn gặp lại ")
        } else {
            event.message.channel.createMessage("Không có trong kênh thoại")
        }
    }

    override val description: String
        get() = "Rời khỏi kênh thoại"
}