package dev.nthduc.hoshino.commands.anime
import dev.kord.x.emoji.Emojis
class FeedCommand : AnimeActionCommand("feed", "MĂMMMMMM ${Emojis.hamburger}", "cho ăn") {
    override val description: String
        get() = "Cho một ai đó ăn"
}