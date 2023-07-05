package dev.nthduc.hoshino.commands.anime
import dev.kord.x.emoji.Emojis
class TickleCommand : AnimeActionCommand("tickle", "KWWWW ${Emojis.laughing}", "cù") {
    override val description: String
        get() = "Cù một ai đó"
}
