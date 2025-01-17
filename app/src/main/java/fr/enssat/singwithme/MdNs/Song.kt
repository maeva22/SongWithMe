package fr.enssat.singwithme.MdNs

import android.content.Context
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.IOException

/*
 * SONG.MP3
 */

fun dowloadSong(context: Context, mdPath: String): String? {

    val mp3Path = mdPath.replace(".md", ".mp3")
    val client = OkHttpClient()
    val request = Request.Builder().url(BASE_URL+mp3Path).build()
    val song = File(context.filesDir, mp3Path.substringAfterLast('/'))

    if (song.exists()) {
        val content = song.readText()
        println("Fichier song trouvé dans le cache : ${song.absolutePath}")
        return content
    }

    try {
        var fileContent : String? = null
        Thread {
            val response = client.newCall(request).execute()
            if (response.isSuccessful) {
                fileContent = response.body?.string()
                fileContent?.let {
                    song.writeText(it)
                    println("Fichier song téléchargé et sauvegardé : ${song.absolutePath}")
                }
            }
        }.start()
        return fileContent

    } catch (e: IOException) {
        e.printStackTrace()
    }
    println("Erreur lors du téléchargement du fichier song.")
    return null
}


