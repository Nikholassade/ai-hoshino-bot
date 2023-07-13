package dev.nthduc.hoshino.extensions.music

import com.kotlindiscord.kord.extensions.extensions.Extension
import com.kotlindiscord.kord.extensions.extensions.chatCommand
import dev.kord.core.Kord
import dev.nthduc.hoshino.commands.music.PlayCommand
import dev.schlaubi.lavakord.LavaKord

class PlayExtension(private val lavalink: LavaKord,private val client: Kord) : Extension(){
    override val name: String
        get() = "play"

    override suspend fun setup() {
        val playCommand = PlayCommand(lavalink, client)

        chatCommand{
            name = "play"
            description = playCommand.description
            aliasKey = "p"
            check { failIf(event.message.author == null) }

            action {

                playCommand.execute(event)
            }

        }


    }
}