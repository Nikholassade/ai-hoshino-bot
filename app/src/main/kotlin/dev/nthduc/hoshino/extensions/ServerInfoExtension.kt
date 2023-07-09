package dev.nthduc.hoshino.extensions

import com.kotlindiscord.kord.extensions.extensions.Extension
import com.kotlindiscord.kord.extensions.extensions.publicSlashCommand
import dev.kord.common.Color
import dev.kord.common.entity.ChannelType
import dev.kord.common.entity.VerificationLevel
import dev.kord.core.entity.Guild
import dev.kord.core.event.interaction.ChatInputCommandInteractionCreateEvent
import dev.kord.rest.Image
import dev.kord.rest.builder.message.create.embed
import dev.nthduc.hoshino.utils.getOwnerInfo
import kotlinx.coroutines.flow.toList
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

class ServerInfoExtension : Extension() {
    override val name = "serverinfo"

    override suspend fun setup() {
        publicSlashCommand {
            name = "serverinfo"
            description = "Hiển thị thông tin về máy chủ"

            action {
                val guild = event.interaction.data.guildId.value?.let { event.kord.getGuild(it) } ?: return@action
                val (userOwner, avatarOwner) = getOwnerInfo(event)

                sendServerInfo(event, guild, userOwner, avatarOwner)
            }
        }
    }

    private suspend fun sendServerInfo(
        event: ChatInputCommandInteractionCreateEvent,
        guild: Guild,
        userOwner: String?,
        avatarOwner: String?
    ) {
        val roles = guild.roles.toList()
            .filterNot { it.name == "@everyone" }
            .take(20)
            .joinToString(", ") { it.mention }
        val channels = guild.channels.toList()
        val textChannels = channels.count { it.type == ChannelType.GuildText }
        val voiceChannels = channels.count { it.type == ChannelType.GuildVoice }

        val verificationLevelName = when (guild.verificationLevel) {
            VerificationLevel.None -> "Không có"
            VerificationLevel.Low -> "Thấp"
            VerificationLevel.Medium -> "Trung bình"
            VerificationLevel.High -> "Cao"
            VerificationLevel.VeryHigh -> "Rất cao"
            else -> "Không xác định"
        }
        val members = guild.memberCount.toString()
        val now = Clock.System.now()
        val created = Instant.fromEpochMilliseconds((guild.id.value.shr(22) + 1420070400000u).toLong())
        val duration = now - created
        val years = duration.inWholeDays / 365
        val months = (duration.inWholeDays % 365) / 30
        val days = (duration.inWholeDays % 365) % 30

        event.interaction.kord.rest.channel.createMessage(event.interaction.channelId) {
            embed {
                author {
                    name = guild.name
                    icon = guild.icon?.cdnUrl?.toUrl { format = Image.Format.PNG } ?: ""
                }
                title = "Thông tin máy chủ"
                color = Color(49, 14, 76)
                thumbnail {
                    url = guild.icon?.cdnUrl?.toUrl { format = Image.Format.PNG } ?: ""
                }
                field {
                    name = "ID Máy chủ"
                    value = guild.id.toString()
                    inline = true
                }
                field {
                    name = "Chủ sở hữu"
                    value =
                        guild.owner.asUser().mention
                    inline =
                        true
                }
                field {
                    name = "Kênh"
                    value = "$textChannels text / $voiceChannels voice"
                    inline = true
                }
                field {
                    name = "Thành viên"
                    value = members
                    inline = true
                }
                field {
                    name = "Cấp độ xác minh"
                    value = verificationLevelName
                    inline = true
                }
                field {
                    name = "Máy chủ được tạo"
                    value = "$years năm, $months tháng, $days ngày trước"
                    inline = true
                }
                field {
                    name = "Vai trò"
                    value = roles.takeIf { it.isNotBlank() } ?: "Không có"
                    inline = true
                }
                footer {
                    text = "Bot được phát triển bởi $userOwner"
                    icon = "$avatarOwner"
                }
                timestamp = Clock.System.now()
            }
        }
    }
}