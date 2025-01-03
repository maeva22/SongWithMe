package fr.enssat.singwithme.MdNs

import android.content.Context
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.IOException
import java.net.URL


/*
 * LYRICS.MD
 */

data class Karaoke(
    val lyrics: List<LyricLine>
)

// Classe pour représenter une ligne de paroles
data class LyricLine(
    val text: String       // Texte de la ligne
)

fun dowloadMd(context: Context, mdPath: String): String? {
    val client = OkHttpClient()
    val request = Request.Builder().url(BASE_URL+mdPath).build()
    val lyricsFile = File(context.cacheDir, mdPath.substringAfterLast('/'))

    if (lyricsFile.exists()) {
        val content = lyricsFile.readText()
        println("Fichier trouvé dans le cache : ${lyricsFile.absolutePath}")
        return content
    }

    try {
        val response = client.newCall(request).execute()
        if (response.isSuccessful) {
            val fileContent = response.body?.string()
            fileContent?.let {
                lyricsFile.writeText(it)
                println("Fichier téléchargé et sauvegardé : ${lyricsFile.absolutePath}")
            }
            return fileContent
        }
    } catch (e: IOException) {
        e.printStackTrace()
    }
    println("Erreur lors du téléchargement du fichier.")
    return null
}


fun parseMd(mdContent: String): Karaoke {
    val lines = mdContent.lines()
    val lyrics = mutableListOf<LyricLine>()

    for (line in lines) {
        when {
            line.startsWith("{") -> {
                val timingEndIndex = line.indexOf('}')
                if (timingEndIndex != -1) {
                    val text = line.substring(timingEndIndex + 1).trim()
                    lyrics.add(LyricLine(text))
                }
            }
        }
    }

    println("Paroles extraites : ${lyrics.map { it.text }}")
    return Karaoke(lyrics)
}


