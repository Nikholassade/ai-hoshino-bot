package dev.nthduc.hoshino.utils

import dev.kord.common.entity.Snowflake
import dev.kord.core.entity.interaction.Interaction
import dev.kord.core.event.interaction.InteractionCreateEvent
import dev.kord.core.event.message.MessageCreateEvent
import kotlinx.coroutines.runBlocking

fun getOwnerInfo(event: MessageCreateEvent): Pair<String?, String?> {
    val userId = 681140407765172232
    val user = runBlocking { event.kord.getUser(Snowflake(userId)) }
    val userOwner = user?.username
    val avatarOwner = user?.avatar?.cdnUrl?.toUrl()
    return Pair(userOwner, avatarOwner)
}

fun getOwnerInfo(interaction: Interaction): Pair<String?, String?> {
    val userId = 681140407765172232
    val user = runBlocking { interaction.kord.getUser(Snowflake(userId)) }
    val userOwner = user?.username
    val avatarOwner = user?.avatar?.cdnUrl?.toUrl()
    return Pair(userOwner, avatarOwner)
}

fun getOwnerInfo(event: InteractionCreateEvent): Pair<String?, String?> {
    val userId = 681140407765172232
    val user = runBlocking { event.kord.getUser(Snowflake(userId)) }
    val userOwner = user?.username
    val avatarOwner = user?.avatar?.cdnUrl?.toUrl()
    return Pair(userOwner, avatarOwner)
}
