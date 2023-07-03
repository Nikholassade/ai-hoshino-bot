package dev.nthduc.hoshino.commands.music

import dev.kord.core.event.message.MessageCreateEvent
import dev.nthduc.hoshino.commands.Command
import dev.schlaubi.lavakord.LavaKord

class NowPlayingCommand(private val lavalink: LavaKord) : Command {
    override suspend fun execute(event: MessageCreateEvent) {
        val link = lavalink.getLink(event.guildId?.toString() ?: return)
        val track = link.player.playingTrack
        if (track != null) {
            event.message.channel.createMessage("Bài hát đang phát: ***${track.title}***")
        } else {
            event.message.channel.createMessage("Không có bài hát nào đang phát")
        }
    }

    override val description: String
        get() = "Bài hát đang phát"
}