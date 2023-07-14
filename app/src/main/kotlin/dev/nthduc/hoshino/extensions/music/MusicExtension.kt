package dev.nthduc.hoshino.extensions.music
import com.kotlindiscord.kord.extensions.extensions.Extension
import com.kotlindiscord.kord.extensions.extensions.chatCommand
import dev.kord.common.Color
import dev.kord.core.behavior.channel.createEmbed
import dev.kord.core.event.interaction.ButtonInteractionCreateEvent
import dev.kord.core.event.message.MessageCreateEvent
import dev.kord.core.exception.EntityNotFoundException
import dev.kord.core.on
import dev.nthduc.hoshino.commands.music.PlayCommand
import dev.nthduc.hoshino.commands.music.TrackPlayer
import dev.nthduc.hoshino.commands.music.queue
import dev.nthduc.hoshino.config.LAVALINK_PASSWORD
import dev.nthduc.hoshino.config.LAVALINK_SERVER
import dev.nthduc.hoshino.embeds.TrackEmbed
import dev.nthduc.hoshino.handlers.ButtonHandler
import dev.nthduc.hoshino.utils.getOwnerInfo
import dev.schlaubi.lavakord.audio.Event
import dev.schlaubi.lavakord.audio.Link
import dev.schlaubi.lavakord.audio.TrackEndEvent
import dev.schlaubi.lavakord.audio.on
import dev.schlaubi.lavakord.kord.lavakord
import dev.schlaubi.lavakord.rest.loadItem
import dev.schlaubi.lavakord.rest.models.TrackResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock

class MusicExtension : Extension() {
    override val name = "play"
    val lavalink = kord.lavakord()
    private var trackEndEventCalled = false
    val playCommand = PlayCommand(lavalink,kord)

    override suspend fun setup() {
        lavalink.addNode(
            serverUri = LAVALINK_SERVER,
            password = LAVALINK_PASSWORD
        )
        chatCommand {
            name = "play"
            description = "Phát nhạc từ Youtube, Soundcloud, Spotify"
            aliasKey = "p"

            action {
                val query = message.content.split(" ").drop(1).joinToString(" ")
                val search = when {
                    query.startsWith("http") -> query
                    query.startsWith("http") && query.contains("soundcloud") -> "scsearch:$query"
                    query.startsWith("http") && query.contains("spotify") -> "spsearch:$query"
                    else -> "ytsearch:$query"
                }
                val (userOwner, avatarOwner) = getOwnerInfo(event)
                val link = lavalink.getLink(event.guildId?.toString() ?: return@action)

                link.player.on<Event, TrackEndEvent> {
                    if (!trackEndEventCalled) {
                        trackEndEventCalled = true
                        CoroutineScope(Dispatchers.Default).launch  {
                            playNextTrack(link, event)
                            trackEndEventCalled = false
                        }
                    }
                }
                if (link.state != Link.State.CONNECTED) {
                    val voiceState = try {
                        event.member?.getVoiceState()
                    } catch (e: EntityNotFoundException) {
                        null
                    }
                    val channelId = voiceState?.channelId
                    if (channelId == null) {
                        event.message.channel.createMessage("Bạn phải tham gia kênh thoại trước ạ !")
                        return@action
                    }
                    link.connectAudio(channelId.value)
                    // Launch a new coroutine to automatically disconnect after 60 seconds of inactivity
                    CoroutineScope(Dispatchers.Default).launch {
                        var lastPlayed = System.currentTimeMillis()
                        while (true) {
                            delay(1000)
                            if (link.player.playingTrack == null) {
                                if (System.currentTimeMillis() - lastPlayed > 60000) {
                                    if (queue.size > 0) queue.clear()
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
                    TrackResponse.LoadType.SEARCH_RESULT -> {
                        link.player.playTrack(item.tracks.first())
                        event.message.channel.createMessage("Đang phát: ${item.tracks.first().info.title}")
                    }
                    TrackResponse.LoadType.PLAYLIST_LOADED -> {
                        queue.addAll(item.tracks)
                        playNextTrack(link, event)
                        event.message.channel.createMessage("Đã thêm playlist vào hàng đợi và bắt đầu phát bài hát đầu tiên")
                        println("Tracks added to queue: ${item.tracks.size}")

                        // Create an embed with a list of the tracks in the playlist
                        event.message.channel.createEmbed {
                            title = "Danh sách phát"
                            description = item.tracks.joinToString("\n") { it.info.title }
                            color = Color(49, 14, 76)
                            timestamp = Clock.System.now()
                            footer {
                                text = "Bot được phát triển bởi $userOwner"
                                icon = "$avatarOwner"
                            }
                        }
                    }
                    TrackResponse.LoadType.NO_MATCHES -> event.message.channel.createMessage("No matches")
                    TrackResponse.LoadType.LOAD_FAILED -> event.message.channel.createMessage(
                        item.exception?.message ?: "Exception"
                    )
                }
            }
        }
        chatCommand {
            name = "skip"
            description = "Skip the currently playing track and move on to the next track in the queue"
            aliasKey = "s"

            action {
                val link = lavalink.getLink(event.guildId?.toString() ?: return@action)
                if (queue.isNotEmpty()) {
                    playNextTrack(link, event)
                    event.message.channel.createMessage("Đã bỏ qua bài hát hiện tại và chuyển sang bài hát tiếp theo.")
                    trackEndEventCalled = false
                } else {
                    event.message.channel.createMessage("Không còn bài hát tiếp theo trong hàng đợi.")
                }
            }
        }

        chatCommand {
            name = "stop"
            description = ""
            check { failIf(event.message.author == null) }

            action {
                val link = lavalink.getLink(event.guildId?.toString() ?: return@action)
                if (link.state == Link.State.CONNECTED) {
                    link.disconnectAudio()
                    queue.clear()
                    link.destroy()
                    event.message.channel.createMessage("Đã dừng phát nhạc ! Hẹn gặp lại ❌ ")
                } else {
                    event.message.channel.createMessage("❌ ***Hiện tại không phát bài hát nào***")
                }
            }
        }

        chatCommand {
            name = "pause"
            description = "Tạm dừng bài hát"
            check { failIf(event.message.author == null) }

            action {
                val link = lavalink.getLink(event.guildId?.toString() ?: return@action)
                if (link.state == Link.State.CONNECTED) {
                    link.player.pause()
                    event.message.channel.createMessage("Đã tạm dừng phát nhạc !")
                } else {
                    event.message.channel.createMessage("Hiện tại không phát bài hát nào")
                }
            }

        }

        chatCommand {
            name = "resume"
            description = "Tiếp tục phát nhạc"
            aliasKey = "re"
            check { failIf(event.message.author == null) }

            action {
                val link = lavalink.getLink(event.guildId?.toString() ?: return@action)
                if (link.state == Link.State.CONNECTED) {
                    link.player.unPause()
                    event.message.channel.createMessage("Đã tiếp tục phát nhạc !")
                } else {
                    event.message.channel.createMessage("Not currently playing a track")
                }
            }
        }

        chatCommand {
            name = "nowplaying"
            description = "Bài hát đang phát"
            aliasKey = "np"
            check { failIf(event.message.author == null) }

            action {
                val link = lavalink.getLink(event.guildId?.toString() ?: return@action)
                val track = link.player.playingTrack
                if (track != null) {
                    event.message.channel.createMessage("Bài hát đang phát: ***${track.title}***")
                } else {
                    event.message.channel.createMessage("Không có bài hát nào đang phát")
                }
            }

        }

        chatCommand {
            name = "leave"
            description = "Rời khỏi kênh thoại"
            check { failIf(event.message.author == null) }

            action {
                val link = lavalink.getLink(event.guildId?.toString() ?: return@action)
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
        }

        chatCommand {
            name = "connect"
            description = "Tham gia kênh thoại"
            aliasKey = "join"
            check { failIf(event.message.author == null) }
            action {
                val link = lavalink.getLink(event.guildId?.toString() ?: return@action)
                if (link.state != Link.State.CONNECTED) {
                    link.connectAudio(event.member?.getVoiceState()?.channelId?.value ?: return@action)
                    event.message.channel.createMessage("Đã vào kênh thoại!")
                } else {
                    event.message.channel.createMessage("Đã có trong kênh thoại")
                }
            }
        }

    }

    private suspend fun playNextTrack(link: Link, event: MessageCreateEvent) {
        val trackPlayer = TrackPlayer(link,queue)
        trackPlayer.playNextTrack(event, true)
        val buttonHandler = ButtonHandler(link, trackPlayer, event, playCommand)
        if (trackPlayer.nextTrack != null) {
            val embed = TrackEmbed(trackPlayer.nextTrack, event).build()

            kord.on<ButtonInteractionCreateEvent> {
                buttonHandler.handleButtonInteraction(this, embed)
            }
        } else {
            event.message.channel.createMessage("Không còn bài hát tiếp theo trong hàng đợi.")
        }
    }
}
