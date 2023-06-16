package hoshino.commands.music

import dev.kord.core.Kord
import dev.kord.core.event.message.MessageCreateEvent
import dev.kord.core.exception.EntityNotFoundException
import dev.schlaubi.lavakord.LavaKord
import dev.schlaubi.lavakord.audio.Link
import dev.schlaubi.lavakord.rest.loadItem
import dev.schlaubi.lavakord.rest.models.TrackResponse
import hoshino.commands.Command
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlayCommand(private val lavalink: LavaKord,private val kord: Kord) : Command {
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
            val voiceState = try {
                event.member?.getVoiceState()
            } catch (e: EntityNotFoundException) {
                null
            }
            val channelId = voiceState?.channelId
            if (channelId == null) {
                event.message.channel.createMessage("Please connect to a voice channel")
                return
            }
            link.connectAudio(channelId.value)
            // Launch a new coroutine to automatically disconnect after 60 seconds of inactivity
            kord.launch {
                var lastPlayed = System.currentTimeMillis()
                while (true) {
                    delay(1000)
                    if (link.player.playingTrack == null) {
                        if (System.currentTimeMillis() - lastPlayed > 60000) {
                            link.destroy()
                            break
                        }
                    } else {
                        lastPlayed = System.currentTimeMillis()
                    }
                }
            }
        }

        val item = link.loadItem(search)

        when (item.loadType) {
            TrackResponse.LoadType.TRACK_LOADED,
            TrackResponse.LoadType.PLAYLIST_LOADED,
            TrackResponse.LoadType.SEARCH_RESULT -> {
                link.player.playTrack(item.tracks.first())
                event.message.channel.createMessage("Đang phát: ${item.tracks.first().info.title}")
            }

            TrackResponse.LoadType.NO_MATCHES -> event.message.channel.createMessage("No matches")
            TrackResponse.LoadType.LOAD_FAILED -> event.message.channel.createMessage(
                item.exception?.message ?: "Exception"
            )
        }
    }

//     suspend fun execute(interaction: Interaction) {
//         val commandInteraction = interaction as? ApplicationCommandInteraction ?: return
//         val options = commandInteraction.data.data.options.value ?: return
//
//        val queryOption = options.firstOrNull { it.name == "query" }
//        val query = queryOption?.value?.value?.toString() ?: return
//        val search = if (query.startsWith("http")) {
//            query
//        } else {
//            "ytsearch:$query"
//        }
//
//         val guildId = commandInteraction.data.guildId.value ?: return
//         val link = lavalink.getLink(guildId.toString())
//         if (link.state != Link.State.CONNECTED) {
//             val member = commandInteraction.data.member.value?.let { Member(it, kord) } ?: return
//             val voiceState = member.getVoiceState()
//             val channelId = voiceState.channelId
//             if (channelId == null) {
//                 commandInteraction.respondPublic {
//                     content = "Please connect to a voice channel"
//                 }
//                 return
//             }
//             link.connectAudio(channelId.value)
//         }
//
//         val item = link.loadItem(search)
//
//         when (item.loadType) {
//             TrackResponse.LoadType.TRACK_LOADED,
//             TrackResponse.LoadType.PLAYLIST_LOADED,
//             TrackResponse.LoadType.SEARCH_RESULT -> link.player.playTrack(
//                 item.tracks.first()
//             )
//
//             TrackResponse.LoadType.NO_MATCHES -> commandInteraction.respondPublic {
//                 content = "No matches"
//             }
//             TrackResponse.LoadType.LOAD_FAILED -> commandInteraction.respondPublic {
//                 content = item.exception?.message ?: "Exception"
//             }
//         }
//     }

    override val description: String
        get() = "Play a song"
}