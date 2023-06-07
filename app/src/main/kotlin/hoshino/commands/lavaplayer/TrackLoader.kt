package hoshino.commands.lavaplayer
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist
import com.sedmelluq.discord.lavaplayer.track.AudioReference
import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import kotlinx.coroutines.future.await
import java.util.concurrent.CompletableFuture

class TrackLoader(private val playerManager: AudioPlayerManager) {
    init {
        AudioSourceManagers.registerRemoteSources(playerManager)
    }

    suspend fun loadTrack(query: String): AudioTrack? {
        val reference = AudioReference(query, null)

        val resultFuture = CompletableFuture<AudioTrack?>()
        val resultHandler = object : AudioLoadResultHandler {
            override fun trackLoaded(track: AudioTrack) {
                resultFuture.complete(track)
            }

            override fun playlistLoaded(playlist: AudioPlaylist) {
                val tracks: List<AudioTrack> = playlist.tracks
                resultFuture.complete(tracks.firstOrNull())
            }

            override fun noMatches() {
                resultFuture.complete(null)
            }

            override fun loadFailed(exception: FriendlyException) {
                resultFuture.completeExceptionally(exception)
            }
        }

        playerManager.loadItem(reference, resultHandler)
        return resultFuture.await()
    }
}
