package dev.nthduc.hoshino.extensions

import com.kotlindiscord.kord.extensions.commands.Arguments
import com.kotlindiscord.kord.extensions.commands.converters.impl.optionalUser
import com.kotlindiscord.kord.extensions.extensions.Extension
import com.kotlindiscord.kord.extensions.extensions.publicMessageCommand
import com.kotlindiscord.kord.extensions.extensions.publicSlashCommand
import com.kotlindiscord.kord.extensions.types.respond
import dev.kord.common.Color
import dev.kord.common.entity.Snowflake
import dev.kord.core.entity.User
import dev.kord.core.event.interaction.MessageCommandInteractionCreateEvent
import dev.kord.rest.Image
import dev.kord.rest.builder.message.create.embed
import dev.nthduc.hoshino.utils.getOwnerInfo
import kotlinx.datetime.Clock

class AvatarExtension : Extension() {
    override val name = "avatar"

    override suspend fun setup() {
        publicSlashCommand(::AvatarSlashArgs) {
            name = "avatar"
            description = "Hiển thị ảnh đại diện của một người dùng"

            action {
                val user = arguments.user ?: user.asUser()
                val (userOwner, avatarOwner) = getOwnerInfo(event)

                respond {
                    embed {
                        title = "Avatar của ${user.username}"
                        author {
                            name = event.interaction.user.username
                            icon = event.interaction.user.avatar?.cdnUrl?.toUrl()
                        }
                        description = "[JPEG](${user.avatar?.cdnUrl?.toUrl{
                            format = Image.Format.JPEG
                            size = Image.Size.Size512
                        }}) | [PNG](${user.avatar?.cdnUrl?.toUrl{
                            format = Image.Format.PNG
                            size = Image.Size.Size512
                        }}) | [WEBP](${user.avatar?.cdnUrl?.toUrl{
                            format = Image.Format.WEBP
                            size = Image.Size.Size512
                        }})"
                        image = user.avatar?.cdnUrl?.toUrl{
                            format = Image.Format.PNG
                            size = Image.Size.Size512
                        }
                        url = user.avatar?.cdnUrl?.toUrl()
                        color = Color(49,14,76)
                        footer {
                            text = "Bot được phát triển bởi $userOwner"
                            icon = "$avatarOwner"
                        }
                        timestamp = Clock.System.now()
                    }
                }
            }
        }

        publicMessageCommand {
            name = "avatar1"

            action {
                val user = getUserFromArgs(event) ?: user.asUser()
                val (userOwner, avatarOwner) = getOwnerInfo(event)

                respond {
                    embed {
                        title = "Avatar của ${user.username}"
                        author {
                            name = event.interaction.user.username
                            icon = event.interaction.user.avatar?.cdnUrl?.toUrl()
                        }
                        description = "[JPEG](${user.avatar?.cdnUrl?.toUrl{
                            format = Image.Format.JPEG
                            size = Image.Size.Size512
                        }}) | [PNG](${user.avatar?.cdnUrl?.toUrl{
                            format = Image.Format.PNG
                            size = Image.Size.Size512
                        }}) | [WEBP](${user.avatar?.cdnUrl?.toUrl{
                            format = Image.Format.WEBP
                            size = Image.Size.Size512
                        }})"
                        image = user.avatar?.cdnUrl?.toUrl{
                            format = Image.Format.PNG
                            size = Image.Size.Size512
                        }
                        url = user.avatar?.cdnUrl?.toUrl()
                        color = Color(49,14,76)
                        footer {
                            text = "Bot được phát triển bởi $userOwner"
                            icon = "$avatarOwner"
                        }
                        timestamp = Clock.System.now()
                    }
                }
            }
        }
    }

    inner class AvatarSlashArgs : Arguments() {
        val user by optionalUser {
            name = "user"
            description = "Người dùng để hiển thị ảnh đại diện"
        }
    }

    private suspend fun getUserFromArgs(event: MessageCommandInteractionCreateEvent): User? {
        val targetMessageContent: String? =
            event.interaction.getTargetOrNull()?.content

        return if (targetMessageContent != null) {
            val args: List<String> =
                targetMessageContent.split(" ")

            if (args.size > 1) {
                val userIdOrName: String =
                    args[1]

                kord.getUser(userIdOrName.toSnowflakeOrNull() ?: return null)
            } else null

        } else null

    }

    private fun String.toSnowflakeOrNull(): Snowflake? =
        toLongOrNull()?.let { Snowflake(it) }
}
