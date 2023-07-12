package dev.nthduc.hoshino.extensions

import com.kotlindiscord.kord.extensions.commands.Arguments
import com.kotlindiscord.kord.extensions.commands.converters.impl.optionalUser
import com.kotlindiscord.kord.extensions.extensions.Extension
import com.kotlindiscord.kord.extensions.extensions.chatCommand
import com.kotlindiscord.kord.extensions.extensions.publicSlashCommand
import com.kotlindiscord.kord.extensions.types.respond
import com.kotlindiscord.kord.extensions.utils.respond
import dev.kord.common.Color
import dev.kord.core.entity.Member
import dev.kord.core.event.interaction.ChatInputCommandInteractionCreateEvent
import dev.kord.core.event.message.MessageCreateEvent
import dev.kord.rest.builder.message.create.embed
import dev.nthduc.hoshino.utils.formatDate
import kotlinx.coroutines.flow.toList

class UserInfoExtension : Extension() {
    override val name = "userinfo"

    override suspend fun setup() {
        publicSlashCommand(::UserInfoArgs) {
            name = "userinfo"
            description = "Hiển thị thông tin về người dùng"

            action {
                val user = arguments.user ?: user.asUser()
                val member = guild?.getMemberOrNull(user.id)

                if (member == null) {
                    respond { content = "Không thể tìm thấy người dùng trong máy chủ này." }
                    return@action
                }

                sendUserInfo(event,member)
            }
        }
        chatCommand(::UserInfoArgs) {
            name = "userinfo"
            description = "Hiển thị thông tin về người dùng"

            action {
                val user = arguments.user ?: user?.asUser()
                val member = guild?.getMemberOrNull(user?.id ?: return@action)

                if (member == null) {
                    message.respond { content = "Không thể tìm thấy người dùng trong máy chủ này." }
                    return@action
                }

                sendUserInfo(event, member)
            }
        }
    }

    private suspend fun sendUserInfo(event: ChatInputCommandInteractionCreateEvent, member: Member) {
        val roles = member.roles.toList()
            .filterNot { it.name == "@everyone" }
            .joinToString(", ") { it.mention }

        event.interaction.kord.rest.channel.createMessage(event.interaction.channelId) {
            embed {
                author {
                    name = member.username
                    icon = member.avatar?.cdnUrl?.toUrl()
                }
                title = "Thông tin người dùng"
                color = Color(49, 14, 76)
                thumbnail {
                    url = member.avatar?.cdnUrl?.toUrl() ?: ""
                }
                field {
                    name = "ID"
                    value = member.id.toString()
                    inline = true
                }
                field {
                    name = "Nickname"
                    value = member.mention
                    inline = true
                }
                field {
                    name = "Ngày tạo tài khoản"
                    value = formatDate(member.id.timestamp)
                    inline = false
                }
                field {
                    name = "Ngày vào máy chủ"
                    value = formatDate(member.joinedAt)
                    inline = true
                }
                field {
                    name = "Vai trò"
                    value = roles.takeIf { it.isNotBlank() } ?: "Không có"
                    inline = false
                }
            }
        }
    }
    private suspend fun sendUserInfo(event: MessageCreateEvent, member: Member) {
        val roles = member.roles.toList()
            .filterNot { it.name == "@everyone" }
            .joinToString(", ") { it.mention }

        event.kord.rest.channel.createMessage(event.message.channelId) {
            embed {
                author {
                    name = member.username
                    icon = member.avatar?.cdnUrl?.toUrl()
                }
                title = "Thông tin người dùng"
                color = Color(49, 14, 76)
                thumbnail {
                    url = member.avatar?.cdnUrl?.toUrl() ?: ""
                }
                field {
                    name = "ID"
                    value = member.id.toString()
                    inline = true
                }
                field {
                    name = "Nickname"
                    value = member.mention
                    inline = true
                }
                field {
                    name = "Ngày tạo tài khoản"
                    value = formatDate(member.id.timestamp)
                    inline = false
                }
                field {
                    name = "Ngày vào máy chủ"
                    value = formatDate(member.joinedAt)
                    inline = true
                }
                field {
                    name = "Vai trò"
                    value = roles.takeIf { it.isNotBlank() } ?: "Không có"
                    inline = false
                }
            }
        }
    }
    inner class UserInfoArgs : Arguments() {
        val user by optionalUser{
            name = "user"
            description =  "Người dùng để hiển thị thông tin"
        }
    }
}
