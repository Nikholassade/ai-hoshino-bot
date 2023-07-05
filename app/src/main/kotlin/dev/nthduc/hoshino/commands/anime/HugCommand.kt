package dev.nthduc.hoshino.commands.anime
import dev.kord.x.emoji.Emojis
class HugCommand : AnimeActionCommand("hug", "SHEEESHHHHH ${Emojis.hugging}", "ôm") {
    override val description: String
        get() = "Ôm một ai đó"
}
