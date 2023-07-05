package dev.nthduc.hoshino.commands.anime
import dev.kord.x.emoji.Emojis

class CuddleCommand : AnimeActionCommand("cuddle", "SHEEESHHHHH ${Emojis.hugging}", "ôm ấp") {
    override val description: String
        get() = "Ôm ấp một ai đó nhẹ nhàng"
}