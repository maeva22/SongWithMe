package fr.enssat.singwithme.MdNs.Back

import android.content.Context
import android.util.Log
import fr.enssat.singwithme.MdNs.Front.BASE_URL
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


/*
 * LYRICS.MD
 */

data class Karaoke(
    val text: String
)

fun downloadMd(context: Context, mdPath: String): String? {

    val client = OkHttpClient()
    val request = Request.Builder().url(BASE_URL +mdPath).build()
    val lyricsFile = File(context.cacheDir, mdPath.substringAfterLast('/'))

    if (lyricsFile.exists()) {
        val content = lyricsFile.readText()
        Log.d("SaveLyrics", "Fichier trouvé dans le cache : ${lyricsFile.absolutePath}.")
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
                Log.d("SaveLyrics", "Fichier téléchargé et sauvegardé : ${lyricsFile.absolutePath}é")
            }
        }
        }.start()
            return fileContent

    } catch (e: IOException) {
        e.printStackTrace()
    }
    Log.d("SaveLyrics", "Erreur lors du téléchargement du fichier.")
    return null
}

// Télécharger le fichier MD depuis une URL dans un thread
fun updownloadMd(url: String, callback: (String?) -> Unit): String? {
    val client = OkHttpClient()
    val request = Request.Builder().url(url).build()
    var fileContent : String? = null
    Thread {
        try {
            val response = client.newCall(request).execute()
            fileContent = if (response.isSuccessful) response.body?.string() else null
            callback(fileContent)
        } catch (e: IOException) {
            e.printStackTrace()
            callback(null)
        }
    }.start()
    return fileContent
}

// Sauvegarder le fichier MD localement
fun saveMd(context: Context, mdString: String, mdPath: String) {
    val lyricsFile = File(context.cacheDir, mdPath.substringAfterLast('/'))
    FileOutputStream(lyricsFile).use { it.write(mdString.toByteArray()) }
}

// Charger le fichier MD local
fun loadMd(context: Context, mdPath: String): String? {
    val lyricsFile = File(context.cacheDir, mdPath.substringAfterLast('/'))
    return if (lyricsFile.exists()) lyricsFile.readText() else null
}

// Télécharger et mettre à jour le fichier MD local
fun downloadAndUpdateMd(context: Context, mdPath: String): String? {
    // Charger le fichier MD local
    val localData = loadMd(context, mdPath)

    // Télécharger le fichier MD distant en arrière-plan
    val fileContent = updownloadMd(BASE_URL + mdPath) { remoteData ->
        if (remoteData != null && remoteData != localData) {
            saveMd(context, remoteData, mdPath)
            Log.d("SaveLyrics", "Mise à jour Lyrics OK")
        }
    }
    return fileContent
}



fun parseMd(mdContent: String): String {
    val lines = mdContent
    return lines
}


