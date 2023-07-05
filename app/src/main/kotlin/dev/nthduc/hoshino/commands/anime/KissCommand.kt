package dev.nthduc.hoshino.commands.anime
import dev.kord.x.emoji.Emojis

class KissCommand : AnimeActionCommand("kiss", "SHEEESHHHHH ${Emojis.couplekiss}", "hôn") {
    override val description: String
        get() = "Hôn một ai đó"
}
