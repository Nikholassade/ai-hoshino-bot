package hoshino.commands.lavaplayer

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist
import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import dev.kord.core.entity.Guild
import dev.kord.core.event.message.MessageCreateEvent
import hoshino.commands.Command
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class PlayCommandDeprecated(private val playerManager: AudioPlayerManager) : Command {
    @OptIn(DelicateCoroutinesApi::class)
    override suspend fun execute(event: MessageCreateEvent) {
        val message = event.message
        val args = message.content.split(" ").drop(1)
        if (args.isEmpty()) {
            message.channel.createMessage("Please provide the name or URL of the song you want to play.")
            return
        }

        val query = args.joinToString(" ")
        val guild = message.getGuild()
        if (guild != null) {
            val musicManager = getMusicManager(guild)
            val trackScheduler = musicManager.trackScheduler

            playerManager.loadItem(query, object : AudioLoadResultHandler {
                override fun trackLoaded(track: AudioTrack) {
                    trackScheduler.queue(track)
                    GlobalScope.launch {
                        message.channel.createMessage("Added `${track.info.title}` to the queue.")
                    }
                }

                override fun playlistLoaded(playlist: AudioPlaylist) {
                    for (track in playlist.tracks) {
                        trackScheduler.queue(track)
                    }
                    GlobalScope.launch {
                        message.channel.createMessage("Added `${playlist.tracks.size}` tracks to the queue.")
                    }
                }

                override fun noMatches() {
                    GlobalScope.launch {
                        message.channel.createMessage("No matches found for `$query`.")
                    }
                }

                override fun loadFailed(exception: FriendlyException) {
                    GlobalScope.launch {
                        message.channel.createMessage("Failed to load `$query`: ${exception.message}")
                    }
                }
            })
        }
    }

    private fun getMusicManager(guild: Guild): MusicManager {
        return musicManagers.getOrPut(guild.id) {
            // Create a new MusicManager for the guild
            MusicManager(playerManager)
        }
    }
    override val description: String
        get() = "Plays a song from YouTube"
}