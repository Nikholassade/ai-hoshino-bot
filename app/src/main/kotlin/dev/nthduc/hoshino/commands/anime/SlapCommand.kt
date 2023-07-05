package dev.nthduc.hoshino.commands.anime
import dev.kord.x.emoji.Emojis
class SlapCommand : AnimeActionCommand("slap", "GRRRRRR ${Emojis.facepalm}", "tát") {
    override val description: String
        get() = "Tát một ai đó"
}
