package dev.nthduc.hoshino.commands.anime
import dev.kord.x.emoji.Emojis
class SmugCommand : AnimeActionCommand("smug", "HHHHH ${Emojis.hugging}", "tỏ vẻ") {
    override val description: String
        get() = "Tự mãn với một ai đó"
}
