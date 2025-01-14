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
    val text: String
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
        var fileContent : String? = null
        Thread {
        val response = client.newCall(request).execute()
        if (response.isSuccessful) {
            fileContent = response.body?.string()
            fileContent?.let {
                lyricsFile.writeText(it)
                println("Fichier téléchargé et sauvegardé : ${lyricsFile.absolutePath}")
            }
        }
        }.start()
            return fileContent

    } catch (e: IOException) {
        e.printStackTrace()
    }
    println("Erreur lors du téléchargement du fichier.")
    return null
}


fun parseMd(mdContent: String): String {
    val lines = mdContent
    return lines
}


