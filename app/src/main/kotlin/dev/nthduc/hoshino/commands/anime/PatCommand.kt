package dev.nthduc.hoshino.commands.anime
import dev.kord.x.emoji.Emojis
class PatCommand : AnimeActionCommand("pat", "KONICHIWAAA ${Emojis.hugging}", "vỗ về") {
    override val description: String
        get() = "Vỗ về một ai đó"
}
