package dev.nthduc.hoshino.commands

import dev.kord.common.Color
import dev.kord.core.behavior.channel.createEmbed
import dev.kord.core.entity.Member
import dev.kord.core.entity.Message
import dev.kord.core.entity.User
import dev.kord.core.event.message.MessageCreateEvent
import dev.nthduc.hoshino.utils.formatDate
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList

@Deprecated("UserInfoCommand Replace UserExtension")
class UserInfoCommand : Command {
    override val description: String
        get() = "Hiển thị thông tin về người dùng"

    override suspend fun execute(event: MessageCreateEvent) {
        val message = event.message
        val user = message.getUserMention()?.asUser() ?: message.author?.asUser()
        val member = message.getGuildOrNull()?.getMemberOrNull(user?.id ?: return)

        if (member == null) {
            message.channel.createMessage("Không thể tìm thấy người dùng trong máy chủ này.")
            return
        }

        sendUserInfo(event, member)
    }

    private suspend fun sendUserInfo(event: MessageCreateEvent, member: Member) {
        val roles = member.roles.toList()
            .filterNot { it.name == "@everyone" }
            .joinToString(", ") { it.mention }

        event.message.channel.createEmbed {
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

    private suspend fun Message.getUserMention(): User? {
        return this.mentionedUsers.firstOrNull()
    }

}
