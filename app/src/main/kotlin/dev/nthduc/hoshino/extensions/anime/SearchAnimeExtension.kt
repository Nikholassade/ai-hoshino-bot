package dev.nthduc.hoshino.extensions.anime

import com.kotlindiscord.kord.extensions.extensions.Extension
import com.kotlindiscord.kord.extensions.extensions.chatCommand
import dev.nthduc.hoshino.commands.anime.SearchAnimeCommand

class SearchAnimeExtension : Extension() {
    override val name: String
        get() = "timnguon"

    override suspend fun setup() {
        val searchAnimeCommand = SearchAnimeCommand()
        chatCommand{
            name = "timnguon"
            description = searchAnimeCommand.description
            check { failIf(event.message.author == null) }

            action {
                searchAnimeCommand.execute(event)
            }
        }
    }
}