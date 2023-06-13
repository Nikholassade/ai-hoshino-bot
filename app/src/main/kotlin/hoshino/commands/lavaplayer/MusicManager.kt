package hoshino.commands.lavaplayer

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager
import dev.kord.common.entity.Snowflake

val musicManagers = mutableMapOf<Snowflake, MusicManager>()

class MusicManager(playerManager: AudioPlayerManager) {
    val player: AudioPlayer = playerManager.createPlayer()
    val trackScheduler = TrackScheduler(player)

    init {
        player.addListener(trackScheduler)
    }
}