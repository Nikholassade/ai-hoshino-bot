package dev.nthduc.hoshino.handlers

import dev.kord.common.entity.ButtonStyle
import dev.kord.core.behavior.interaction.respondPublic
import dev.kord.core.event.interaction.ButtonInteractionCreateEvent
import dev.kord.rest.builder.component.ActionRowBuilder
import dev.kord.rest.builder.message.EmbedBuilder
import dev.schlaubi.lavakord.audio.Link

class ButtonHandler(private val link: Link) {
    suspend fun handleButtonInteraction(interaction: ButtonInteractionCreateEvent, embed: EmbedBuilder) {
        when (interaction.interaction.componentId) {
            "pauseBtn" -> {
                // Pause the track
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
                    })
                }
            }
            "resumeBtn" -> {
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
                    })
                }
            }
        }
        }
    }