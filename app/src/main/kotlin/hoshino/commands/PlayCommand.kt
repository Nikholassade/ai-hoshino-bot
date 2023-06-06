package hoshino.commands
import dev.kord.core.Kord
import dev.kord.core.event.message.MessageCreateEvent
import dev.schlaubi.lavakord.MutableLavaKordOptions
import dev.schlaubi.lavakord.audio.TrackStartEvent
import dev.schlaubi.lavakord.kord.getLink
import dev.schlaubi.lavakord.rest.loadItem
import hoshino.commands.music.KordLavaKord
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import dev.schlaubi.lavakord.rest.models.TrackResponse.LoadType


class PlayCommand(val kord: Kord) : Command {
    override suspend fun execute(event: MessageCreateEvent) {
        // Get the message and its content
        val message = event.message
        val content = message.content

        // Parse the command arguments
        val args = content.split(" ").drop(1)
        val query = args.joinToString(" ")

        // Get the guild and voice channel
        val guild = event.getGuildOrNull()
        val voiceChannel = event.message.getAuthorAsMemberOrNull()?.getVoiceStateOrNull()?.getChannelOrNull()
        if (guild != null && voiceChannel != null) {
            // Create an instance of LavaKord
            val options = MutableLavaKordOptions()
            val lavaKord = KordLavaKord(kord, kord.selfId.value, options)
            lavaKord.addNode(
                serverUri = "wss://lavalink-replit.nthduc.repl.co:443",
                password = "021220"
            )

            // Get the link for the guild
            val link = guild.getLink(lavaKord)

            // Connect to the voice channel
            link.connectAudio(voiceChannel.id.value)

            // Load and play the track
            var result = link.loadItem(query)
            var attempts = 0
            while (result.loadType == LoadType.NO_MATCHES && attempts < 5) {
                delay(1000)
                result = link.loadItem(query)
                attempts++
            }
            if (result.tracks.isNotEmpty()) {
                val track = result.tracks.first()
                link.player.playTrack(track)
            } else {
                message.channel.createMessage("No tracks were found for the query `$query`.")
            }

            // Listen for the TrackStartEvent
            link.node.events.filterIsInstance<TrackStartEvent>().onEach { trackStartEvent ->
                kord.launch {
                    message.channel.createMessage("Now playing `${trackStartEvent.getTrack().title}`")
                }
            }.launchIn(kord)
        } else {
            message.channel.createMessage("You must join a voice channel first.")
        }
    }

    override val description: String
        get() = "Plays a song"
}