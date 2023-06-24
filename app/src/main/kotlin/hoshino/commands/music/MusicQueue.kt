package hoshino.commands.music

import dev.kord.common.entity.Snowflake
import dev.schlaubi.lavakord.rest.models.PartialTrack

class MusicQueue(private val guildId: Snowflake) {
    private val queue = mutableListOf<PartialTrack>()

    fun addTrack(track: PartialTrack) {
        queue.add(track)
    }

    fun removeTrack(track: PartialTrack) {
        queue.remove(track)
    }

    fun getNextTrack(): PartialTrack? {
        return if (queue.isNotEmpty()) queue.removeAt(0) else null
    }

    fun clear() {
        queue.clear()
    }

    companion object {
        private val queues = mutableMapOf<Snowflake, MusicQueue>()

        fun getQueue(guildId: Snowflake): MusicQueue {
            return queues.getOrPut(guildId) { MusicQueue(guildId) }
        }
    }
}