package dev.nthduc.hoshino.extensions.anime
import com.kotlindiscord.kord.extensions.commands.Arguments
import com.kotlindiscord.kord.extensions.commands.converters.impl.optionalUser
import com.kotlindiscord.kord.extensions.extensions.Extension
import com.kotlindiscord.kord.extensions.extensions.chatCommand
import com.kotlindiscord.kord.extensions.extensions.publicSlashCommand
import com.kotlindiscord.kord.extensions.types.respond
import dev.nthduc.hoshino.commands.anime.PatCommand

class PatExtension : Extension() {
    override val name = "pat"

    override suspend fun setup() {
        val patCommand = PatCommand()

        publicSlashCommand(::PatArgs) {
            name = "pat"
            description = patCommand.description

            action {
                respond {
                    content = "Đang xử lí ..."
                }
                patCommand.execute(event)
            }
        }

        chatCommand(::PatArgs) {
            name = "pat"
            description = patCommand.description

            check { failIf(event.message.author == null) }

            action {
                patCommand.execute(event)
            }
        }
    }

    inner class PatArgs : Arguments() {
        val user by optionalUser {
            name = "user"
            description = "Người dùng để vỗ về một ai đó"
        }
    }
}
