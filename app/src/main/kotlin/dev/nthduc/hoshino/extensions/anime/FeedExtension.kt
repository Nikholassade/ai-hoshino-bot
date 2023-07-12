package dev.nthduc.hoshino.extensions.anime
import com.kotlindiscord.kord.extensions.commands.Arguments
import com.kotlindiscord.kord.extensions.commands.converters.impl.optionalUser
import com.kotlindiscord.kord.extensions.extensions.Extension
import com.kotlindiscord.kord.extensions.extensions.chatCommand
import com.kotlindiscord.kord.extensions.extensions.publicSlashCommand
import com.kotlindiscord.kord.extensions.types.respond
import dev.nthduc.hoshino.commands.anime.FeedCommand

class FeedExtension : Extension() {
    override val name = "feed"

    override suspend fun setup() {
        val cuddleCommand = FeedCommand()

        publicSlashCommand(::FeedArgs) {
            name = "feed"
            description = cuddleCommand.description

            action {
                respond {
                    content = "Đang xử lí ..."
                }
                cuddleCommand.execute(event)
            }
        }

        chatCommand(::FeedArgs) {
            name = "feed"
            description = cuddleCommand.description

            check { failIf(event.message.author == null) }

            action {
                cuddleCommand.execute(event)
            }
        }
    }

    inner class FeedArgs : Arguments() {
        val user by optionalUser {
            name = "user"
            description = "Người dùng để cho một ai đó ăn"
        }
    }
}
