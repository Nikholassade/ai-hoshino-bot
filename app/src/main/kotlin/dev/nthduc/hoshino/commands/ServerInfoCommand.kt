package dev.nthduc.hoshino.commands

import dev.kord.common.Color
import dev.kord.common.entity.ChannelType
import dev.kord.common.entity.Snowflake
import dev.kord.common.entity.VerificationLevel
import dev.kord.common.entity.optional.value
import dev.kord.core.behavior.interaction.respondPublic
import dev.kord.core.entity.Guild
import dev.kord.core.entity.interaction.ApplicationCommandInteraction
import dev.kord.core.entity.interaction.Interaction
import dev.kord.core.event.message.MessageCreateEvent
import dev.kord.rest.Image
import dev.kord.rest.builder.message.create.embed
import dev.nthduc.hoshino.utils.getOwnerInfo
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

class ServerInfoCommand() : Command {
    override suspend fun execute(event: MessageCreateEvent) {
        val message = event.message
        val guild = message.getGuild()
        val (userOwner, avatarOwner) = getOwnerInfo(event)
        sendServerInfo(guild, message.channelId,userOwner,avatarOwner)
    }

    suspend fun execute(interaction: Interaction) {
        val commandInteraction =
            interaction as ApplicationCommandInteraction
        val (userOwner, avatarOwner) = getOwnerInfo(interaction)
        val guild =
            commandInteraction.data.guildId.value?.let { interaction.kord.getGuildOrNull(it) } ?: return
        interaction.kord.launch {
            commandInteraction.respondPublic {
                content = "Đang lấy thông tin máy chủ..."
            }

            sendServerInfo(guild, commandInteraction.channelId, userOwner,avatarOwner)
        }
    }


    private suspend fun sendServerInfo(guild: Guild, channelId: Snowflake, userOwner: String?, avatarOwner: String? ) {
        val roles = guild.kord.rest.guild.getGuildRoles(guild.id)
            .filterNot { it.name == "@everyone" }
            .take(20)
            .map { "<@&${it.id.value}>" }
            .joinToString(", ")
        val channels = guild.channels.toList()
        val textChannels = channels.count { it.type == ChannelType.GuildText }
        val voiceChannels = channels.count { it.type == ChannelType.GuildVoice }
        val verificationLevelName = when (guild.verificationLevel) {
            VerificationLevel.None -> "None"
            VerificationLevel.Low -> "Low"
            VerificationLevel.Medium -> "Medium"
            VerificationLevel.High -> "High"
            VerificationLevel.VeryHigh -> "Very High"
            else -> "Unknown"
        }
        val members = guild.data.memberCount.value.toString()
        val now = Clock.System.now()
        val created = Instant.fromEpochMilliseconds((guild.id.value.shr(22) + 1420070400000u).toLong())
        val duration = now - created
        val years = duration.inWholeDays / 365
        val months = (duration.inWholeDays % 365) / 30
        val days = (duration.inWholeDays % 365) % 30

        guild.kord.rest.channel.createMessage(channelId) {
            embed {
                author {
                    name = guild.name
                    icon = guild.icon?.cdnUrl?.toUrl { format = Image.Format.PNG } ?: ""
                }
                title = guild.name
                color = Color(49, 14, 76)
                thumbnail {
                    url = guild.icon?.cdnUrl?.toUrl { format = Image.Format.PNG } ?: ""
                }
                field {
                    name = "Server ID"
                    value = guild.id.toString()
                    inline = true
                }
                field {
                    name = "Server Owner"
                    value =
                        guild.owner.asUser().mention
                    inline =
                        true
                }
                field {
                    name = "Channels"
                    value = "$textChannels text / $voiceChannels voice"
                    inline = true
                }
                field {
                    name = "Members"
                    value = members
                    inline = true
                }
                field {
                    name = "Verification Level"
                    value = verificationLevelName
                    inline = true
                }
                field {
                    name = "Server Created"
                    value = "$years years, $months months, $days days ago"
                    inline = true
                }
                field {
                    name = "Roles"
                    value = roles.takeIf { it.isNotBlank() } ?: "None"
                    inline = true
                }
                footer {
                    text = "Bot được phát triển bởi $userOwner"
                    icon = "$avatarOwner"
                }
            }
        }
    }

    override val description: String
        get() = "Hiển thị thông tin về Server"
}