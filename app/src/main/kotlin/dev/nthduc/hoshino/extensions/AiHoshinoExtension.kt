package dev.nthduc.hoshino.extensions

import com.kotlindiscord.kord.extensions.commands.Arguments
import com.kotlindiscord.kord.extensions.commands.converters.impl.optionalUser
import com.kotlindiscord.kord.extensions.extensions.Extension
import com.kotlindiscord.kord.extensions.extensions.chatCommand
import com.kotlindiscord.kord.extensions.extensions.publicSlashCommand
import com.kotlindiscord.kord.extensions.types.respond
import com.kotlindiscord.kord.extensions.utils.respond
import dev.kord.common.Color
import dev.kord.rest.builder.message.create.embed
import dev.kord.x.emoji.Emojis
import dev.nthduc.hoshino.utils.getOwnerInfo
import io.ktor.client.request.forms.*
import io.ktor.utils.io.jvm.javaio.*
import kotlinx.datetime.Clock
import java.io.File
import java.io.FileInputStream

class AiHoshinoExtension : Extension() {
    override val name = "aihoshino"

    override suspend fun setup() {
        publicSlashCommand(::AiHoshinoArgs) {
            name = "aihoshino"
            description = "Gửi lời yêu thương đến người dùng được chỉ định"

            action {
                val user = arguments.user ?: user.asUser()
                val (userOwner, avatarOwner) = getOwnerInfo(event)

                respond {
                    content = "I love you ${Emojis.heart} ${user.mention}!"
                    embed {
                        color = Color(49, 14, 76)
                        image = "attachment://oshi-no-ko-anime.gif"
                        footer {
                            text = "Bot được phát triển bởi $userOwner"
                            icon = "$avatarOwner"
                        }
                        timestamp = Clock.System.now()
                    }
                    val url = this::class.java.getResource("/images/oshi-no-ko-anime.gif")
                    if (url != null) {
                        val fileInputStream = FileInputStream(File(url.toURI()))
                        val byteReadChannel = fileInputStream.toByteReadChannel()
                        val channelProvider = ChannelProvider(size = File(url.toURI()).length()) { byteReadChannel }
                        addFile("oshi-no-ko-anime.gif", channelProvider)
                    }
                }
            }
        }
        chatCommand(::AiHoshinoArgs) {
            name = "aihoshino"
            description = "Gửi lời yêu thương đến người dùng được chỉ định"
            aliasKey = "hoshino"

            action {
                val user = arguments.user ?: user?.asUser()
                val (userOwner, avatarOwner) = getOwnerInfo(event)

                message.respond {
                    content = "I love you ${Emojis.heart} ${user?.mention}!"
                    embed {
                        color = Color(49, 14, 76)
                        image = "attachment://oshi-no-ko-anime.gif"
                        footer {
                            text = "Bot được phát triển bởi $userOwner"
                            icon = "$avatarOwner"
                        }
                        timestamp = Clock.System.now()
                    }
                    val url = this::class.java.getResource("/images/oshi-no-ko-anime.gif")
                    if (url != null) {
                        val fileInputStream = FileInputStream(File(url.toURI()))
                        val byteReadChannel = fileInputStream.toByteReadChannel()
                        val channelProvider = ChannelProvider(size = File(url.toURI()).length()) { byteReadChannel }
                        addFile("oshi-no-ko-anime.gif", channelProvider)
                    }
                }
            }
        }
    }

    inner class AiHoshinoArgs : Arguments() {
        val user by optionalUser{
            name = "user"
            description = "I love you"
        }
    }
}
