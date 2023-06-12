package hoshino.commands.music
import dev.kord.core.event.message.MessageCreateEvent
import dev.schlaubi.lavakord.LavaKord
import dev.schlaubi.lavakord.audio.Link
import hoshino.commands.Command
class ConnectCommand(private val lavalink: LavaKord) : Command {
    override suspend fun execute(event: MessageCreateEvent) {
        val link = lavalink.getLink(event.guildId?.toString() ?: return)
        if (link.state != Link.State.CONNECTED) {
            link.connectAudio(event.member?.getVoiceState()?.channelId?.value ?: return)
            event.message.channel.createMessage("Đã vào kênh thoại!")
        } else {
            event.message.channel.createMessage("Đã có trong kênh thoại")
        }
    }

    override val description: String
        get() = "Vào kênh thoại"
}