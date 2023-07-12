package dev.nthduc.hoshino.extensions.anime
import com.kotlindiscord.kord.extensions.commands.Arguments
import com.kotlindiscord.kord.extensions.commands.converters.impl.optionalUser
import com.kotlindiscord.kord.extensions.extensions.Extension
import com.kotlindiscord.kord.extensions.extensions.chatCommand
import com.kotlindiscord.kord.extensions.extensions.publicSlashCommand
import com.kotlindiscord.kord.extensions.types.respond
import dev.nthduc.hoshino.commands.anime.HugCommand

class HugExtension : Extension() {
    override val name = "hug"

    override suspend fun setup() {
        val cuddleCommand = HugCommand()

        publicSlashCommand(::HugArgs) {
            name = "hug"
            description = cuddleCommand.description

            action {
                respond {
                    content = "Đang xử lí ..."
                }
                cuddleCommand.execute(event)
            }
        }

        chatCommand(::HugArgs) {
            name = "hug"
            description = cuddleCommand.description

            check { failIf(event.message.author == null) }

            action {
                cuddleCommand.execute(event)
            }
        }
    }

    inner class HugArgs : Arguments() {
        val user by optionalUser {
            name = "user"
            description = "Người dùng để cho một ai đó ăn"
        }
    }
}
