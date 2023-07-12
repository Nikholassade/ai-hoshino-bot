package dev.nthduc.hoshino.extensions.anime
import com.kotlindiscord.kord.extensions.commands.Arguments
import com.kotlindiscord.kord.extensions.commands.converters.impl.optionalUser
import com.kotlindiscord.kord.extensions.extensions.Extension
import com.kotlindiscord.kord.extensions.extensions.chatCommand
import com.kotlindiscord.kord.extensions.extensions.publicSlashCommand
import com.kotlindiscord.kord.extensions.types.respond
import dev.nthduc.hoshino.commands.anime.SmugCommand

class SmugExtension : Extension() {
    override val name = "smug"

    override suspend fun setup() {
        val smugCommand = SmugCommand()

        publicSlashCommand(::SmugArgs) {
            name = "smug"
            description = smugCommand.description

            action {
                respond {
                    content = "Đang xử lí ..."
                }
                smugCommand.execute(event)
            }
        }

        chatCommand(::SmugArgs) {
            name = "smug"
            description = smugCommand.description

            check { failIf(event.message.author == null) }

            action {
                smugCommand.execute(event)
            }
        }
    }

    inner class SmugArgs : Arguments() {
        val user by optionalUser {
            name = "user"
            description = "Người dùng để Tự mãn với một ai đó"
        }
    }
}
