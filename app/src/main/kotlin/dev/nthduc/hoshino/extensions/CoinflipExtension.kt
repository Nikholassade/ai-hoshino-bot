package dev.nthduc.hoshino.extensions

import com.kotlindiscord.kord.extensions.extensions.Extension
import com.kotlindiscord.kord.extensions.extensions.chatCommand
import com.kotlindiscord.kord.extensions.extensions.publicSlashCommand
import com.kotlindiscord.kord.extensions.types.respond
import com.kotlindiscord.kord.extensions.utils.respond
import kotlin.random.Random

class CoinflipExtension : Extension() {
    override val name = "coinflip"

    override suspend fun setup() {
        publicSlashCommand {
            name = "coinflip"
            description = "Lật xu"

            action {
                val result = if (Random.nextBoolean()) "mặt sấp" else "mặt ngửa"
                respond { content = "Đồng xu rơi vào `$result`!" }
            }
        }
        chatCommand{
            name = "coinflip"
            description = "Lật xu"
            aliasKey = "cf"

            check { failIf(event.message.author == null) }

            action {
                val result = if (Random.nextBoolean()) "mặt sấp" else "mặt ngửa"
                message.respond {
                    content = "Đồng xu rơi vào `$result`!"
                }
            }
        }

    }
}