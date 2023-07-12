package dev.nthduc.hoshino.extensions.anime
import com.kotlindiscord.kord.extensions.commands.Arguments
import com.kotlindiscord.kord.extensions.commands.converters.impl.optionalUser
import com.kotlindiscord.kord.extensions.extensions.Extension
import com.kotlindiscord.kord.extensions.extensions.chatCommand
import com.kotlindiscord.kord.extensions.extensions.publicSlashCommand
import com.kotlindiscord.kord.extensions.types.respond
import dev.nthduc.hoshino.commands.anime.TickleCommand

class TickleExtension : Extension() {
    override val name = "tickle"

    override suspend fun setup() {
        val tickleCommand = TickleCommand()

        publicSlashCommand(::TickleArgs) {
            name = "tickle"
            description = tickleCommand.description

            action {
                respond {
                    content = "Đang xử lí ..."
                }
                tickleCommand.execute(event)
            }
        }

        chatCommand(::TickleArgs) {
            name = "tickle"
            description = tickleCommand.description

            check { failIf(event.message.author == null) }

            action {
                tickleCommand.execute(event)
            }
        }
    }

    inner class TickleArgs : Arguments() {
        val user by optionalUser {
            name = "user"
            description = "Người dùng để Tự mãn với một ai đó"
        }
    }
}
