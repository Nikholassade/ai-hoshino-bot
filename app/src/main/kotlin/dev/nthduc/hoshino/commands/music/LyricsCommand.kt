package dev.nthduc.hoshino.commands.music

import dev.kord.core.event.message.MessageCreateEvent
import dev.nthduc.hoshino.commands.Command
import dev.schlaubi.lavakord.LavaKord
import org.json.JSONObject
import java.net.URI
import java.net.URLEncoder
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

class LyricsCommand(private val lavalink: LavaKord) : Command {
    override suspend fun execute(event: MessageCreateEvent) {
        val link = lavalink.getLink(event.guildId?.toString() ?: return)
        val track = link.player.playingTrack
        if (track != null) {
            val title = track.title
            val artist = track.author
            val lyrics = getLyrics(title, artist)
            if (lyrics != null) {
                event.message.channel.createMessage(lyrics)
            } else {
                event.message.channel.createMessage("Không tìm thấy lời bài hát cho bài hát này")
            }
        } else {
            event.message.channel.createMessage("Không có bài hát nào đang phát")
        }
    }

    override val description: String
        get() = "Hiện thị lời vài hát"

    private suspend fun getLyrics(title: String, artist: String): String? {
        val apiKey = "934aaeed9f007837bef3cc0795918ef5"
        val url = "https://api.musixmatch.com/ws/1.1/matcher.lyrics.get?q_track=${URLEncoder.encode(title, "UTF-8")}&q_artist=${URLEncoder.encode(artist, "UTF-8")}&apikey=$apiKey"
        val request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .build()
        val httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .build()
        val response = httpClient.send(request, HttpResponse.BodyHandlers.ofString())
        val json = JSONObject(response.body())
        println(json)
        val statusCode = json.getJSONObject("message").getJSONObject("header").getInt("status_code")
        if (statusCode == 200) {
            return json.getJSONObject("message").getJSONObject("body").getJSONObject("lyrics").getString("lyrics_body")
        } else {
            return null
        }
    }




}
