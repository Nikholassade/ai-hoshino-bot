package dev.nthduc.hoshino.extensions.anime
import com.kotlindiscord.kord.extensions.commands.Arguments
import com.kotlindiscord.kord.extensions.commands.converters.impl.optionalUser
import com.kotlindiscord.kord.extensions.extensions.Extension
import com.kotlindiscord.kord.extensions.extensions.chatCommand
import com.kotlindiscord.kord.extensions.extensions.publicSlashCommand
import com.kotlindiscord.kord.extensions.types.respond
import dev.nthduc.hoshino.commands.anime.SlapCommand

class SlapExtension : Extension() {
    override val name = "slap"

    override suspend fun setup() {
        val slapCommand = SlapCommand()

        publicSlashCommand(::SlapArgs) {
            name = "slap"
            description = slapCommand.description

            action {
                respond {
                    content = "Đang xử lí ..."
                }
                slapCommand.execute(event)
            }
        }

        chatCommand(::SlapArgs) {
            name = "slap"
            description = slapCommand.description

            check { failIf(event.message.author == null) }

            action {
                slapCommand.execute(event)
            }
        }
    }

    inner class SlapArgs : Arguments() {
        val user by optionalUser {
            name = "user"
            description = "Người dùng để tát một ai đó"
        }
    }
}
