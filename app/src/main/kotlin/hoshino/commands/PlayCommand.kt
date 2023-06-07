package hoshino.commands

import dev.kord.core.event.message.MessageCreateEvent
import dev.schlaubi.lavakord.LavaKord
import dev.schlaubi.lavakord.audio.Link
import dev.schlaubi.lavakord.rest.loadItem
import dev.schlaubi.lavakord.rest.models.TrackResponse

class PlayCommand(private val lavalink: LavaKord) : Command {
    override suspend fun execute(event: MessageCreateEvent) {
        val args = event.message.content.split(" ")
        val query = args.drop(1).joinToString(" ")
        val search = if (query.startsWith("http")) {
            query
        } else {
            "ytsearch:$query"
        }

        val link = lavalink.getLink(event.guildId?.toString() ?: return)
        if (link.state != Link.State.CONNECTED) {
            val voiceState = event.member?.getVoiceState()
            val channelId = voiceState?.channelId
            if (channelId == null) {
                event.message.channel.createMessage("Please connect to a voice channel")
                return
            }
            link.connectAudio(channelId.value)
        }

        val item = link.loadItem(search)

        when (item.loadType) {
            TrackResponse.LoadType.TRACK_LOADED,
            TrackResponse.LoadType.PLAYLIST_LOADED,
            TrackResponse.LoadType.SEARCH_RESULT -> link.player.playTrack(
                item.tracks.first()
            )

            TrackResponse.LoadType.NO_MATCHES -> event.message.channel.createMessage("No matches")
            TrackResponse.LoadType.LOAD_FAILED -> event.message.channel.createMessage(
                item.exception?.message ?: "Exception"
            )
        }
    }

    override val description: String
        get() = "Play a song"
}