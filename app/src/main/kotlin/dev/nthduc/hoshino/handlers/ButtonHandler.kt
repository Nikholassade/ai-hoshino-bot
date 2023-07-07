package dev.nthduc.hoshino.handlers

import dev.kord.common.entity.ButtonStyle
import dev.kord.core.behavior.interaction.respondPublic
import dev.kord.core.event.interaction.ButtonInteractionCreateEvent
import dev.kord.core.event.message.MessageCreateEvent
import dev.kord.rest.builder.component.ActionRowBuilder
import dev.kord.rest.builder.message.EmbedBuilder
import dev.nthduc.hoshino.commands.music.PlayCommand
import dev.nthduc.hoshino.commands.music.TrackPlayer
import dev.nthduc.hoshino.commands.music.queue
import dev.schlaubi.lavakord.audio.Link
import kotlinx.coroutines.launch

class ButtonHandler(private val link: Link,
                    private val trackPlayer: TrackPlayer,
                    private val event: MessageCreateEvent,
                    private val playCommand: PlayCommand
) {
    suspend fun handleButtonInteraction(interaction: ButtonInteractionCreateEvent, embed: EmbedBuilder) {
        when (interaction.interaction.componentId) {
            "pauseBtn" -> {
                interaction.kord.launch {
                    // Pause the track
                    if(link.state == Link.State.CONNECTED) {
                        link.player.pause()
                        interaction.interaction.respondPublic {
                            embeds.add(embed)
                            content = "Đã dừng phát nhạc"
                            components.add(ActionRowBuilder().apply {
                                interactionButton(ButtonStyle.Primary, "pauseBtn") {
                                    label = "Tạm dừng phát"
                                    disabled = true
                                }
                                interactionButton(ButtonStyle.Secondary, "resumeBtn") {
                                    label = "Tiếp tục phát"
                                    disabled = false
                                }
                                interactionButton(ButtonStyle.Secondary, "skipBtn") {
                                    label = "Bài tiếp theo"
                                    disabled = queue.size < 1
                                }
                            })
                        }
                    }
                }
            }

            "resumeBtn" -> {
                interaction.kord.launch {
                    // Resume the track
                        link.player.unPause()
                        interaction.interaction.respondPublic {
                            embeds.add(embed)
                            content = "Đã tiếp tục phát nhạc"
                            components.add(ActionRowBuilder().apply {
                                interactionButton(ButtonStyle.Primary, "pauseBtn") {
                                    label = "Tạm dừng phát"
                                    disabled = false
                                }
                                interactionButton(ButtonStyle.Secondary, "resumeBtn") {
                                    label = "Tiếp tục phát"
                                    disabled = true
                                }
                                interactionButton(ButtonStyle.Success, "skipBtn") {
                                    label = "Bài tiếp theo"
                                    disabled = queue.size < 1
                                }
                            })
                        }
                }
            }

            "skipBtn" -> {
                interaction.kord.launch {
                    trackPlayer.playNextTrack(queue,event,false)
                    playCommand.trackEndEventCalled = false;

                    if(link.state == Link.State.CONNECTED) {
                        interaction.interaction.respondPublic {
                            content = "Đã bỏ qua bài hát hiện tại"
                        }
                    }
                }
            }
        }
    }
}