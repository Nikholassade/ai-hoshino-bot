package dev.nthduc.hoshino.extensions.anime

import com.kotlindiscord.kord.extensions.extensions.Extension
import com.kotlindiscord.kord.extensions.extensions.chatCommand
import dev.nthduc.hoshino.commands.anime.SearchAnimeSauceNaoCommand

class SearchAnimeSauceNaoExtension : Extension() {
    override val name: String
        get() = "sauce"

    override suspend fun setup() {
        val searchAnimeSauceNao = SearchAnimeSauceNaoCommand()

        chatCommand {
            name = "sauce"
            description = searchAnimeSauceNao.description
            check { failIf(event.message.author == null) }

            action {
                searchAnimeSauceNao.execute(event)
            }

        }
    }
}