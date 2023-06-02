package hoshino.commands

import dev.kord.core.event.message.MessageCreateEvent

class AvatarCommand : Command {
    override suspend fun execute(event: MessageCreateEvent) {
        val message = event.message
        val mentionedUser = message.getGuildOrNull()?.id?.let { message.author?.asMemberOrNull(it) } ?: return
        val avatarUrl = mentionedUser.avatar

        message.channel.createMessage("Avatar của người dùng là: $avatarUrl")
    }
}
