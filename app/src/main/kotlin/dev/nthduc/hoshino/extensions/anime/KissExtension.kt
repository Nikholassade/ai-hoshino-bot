package dev.nthduc.hoshino.extensions.anime
import com.kotlindiscord.kord.extensions.commands.Arguments
import com.kotlindiscord.kord.extensions.commands.converters.impl.optionalUser
import com.kotlindiscord.kord.extensions.extensions.Extension
import com.kotlindiscord.kord.extensions.extensions.chatCommand
import com.kotlindiscord.kord.extensions.extensions.publicSlashCommand
import com.kotlindiscord.kord.extensions.types.respond
import dev.nthduc.hoshino.commands.anime.KissCommand

class KissExtension : Extension() {
    override val name = "kiss"

    override suspend fun setup() {
        val cuddleCommand = KissCommand()

        publicSlashCommand(::KissArgs) {
            name = "kiss"
            description = cuddleCommand.description

            action {
                respond {
                    content = "Đang xử lí ..."
                }
                cuddleCommand.execute(event)
            }
        }

        chatCommand(::KissArgs) {
            name = "kiss"
            description = cuddleCommand.description

            check { failIf(event.message.author == null) }

            action {
                cuddleCommand.execute(event)
            }
        }
    }

    inner class KissArgs : Arguments() {
        val user by optionalUser {
            name = "user"
            description = "Người dùng để cho một ai đó ăn"
        }
    }
}
