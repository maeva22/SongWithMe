package fr.enssat.singwithme.MdNs

import android.content.Context
import android.content.Context.DOWNLOAD_SERVICE
import android.net.Uri
import android.util.Log
import androidx.annotation.OptIn
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.net.toUri
import androidx.media3.common.MediaItem
import androidx.media3.common.MimeTypes
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.hls.HlsMediaSource
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.BufferedInputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL


/*
 * SONG.MP3
 */

fun downloadSong(context: Context, mdPath: String): File? {

    val mp3Path = mdPath.replace(".md", ".mp3")
    val outputFile = File(context.filesDir, mp3Path.substringAfterLast('/'))

    // Si le fichier existe déjà, le retourner
    if (outputFile.exists()) {
        Log.d("DownloadFile", "Fichier déjà téléchargé : ${outputFile.absolutePath}")
        return outputFile
    }

    try {
        val url = URL(BASE_URL+mp3Path)
        val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
        connection.connect()

        // Vérifier si la connexion est réussie
        if (connection.responseCode != HttpURLConnection.HTTP_OK) {
            Log.e("DownloadFile", "Erreur HTTP : ${connection.responseCode}")
            return null
        }

        val lengthOfFile = connection.contentLength
        Log.d("DownloadFile", "Taille du fichier : $lengthOfFile")

        // Lire le fichier depuis le flux
        val input: InputStream = BufferedInputStream(connection.inputStream)
        val output = FileOutputStream(outputFile)

        val data = ByteArray(1024)
        var total: Long = 0
        var count: Int

        while (input.read(data).also { count = it } != -1) {
            total += count
            output.write(data, 0, count)

            // Afficher la progression dans les logs
            Log.d("DownloadFile", "Progression : ${(total * 100) / lengthOfFile}%")
        }

        // Fermer les flux
        output.flush()
        output.close()
        input.close()

        Log.d("DownloadFile", "Fichier téléchargé avec succès : ${outputFile.absolutePath}")
        return outputFile
    } catch (e: Exception) {
        Log.e("DownloadFile", "Erreur lors du téléchargement : ${e.message}")
        return null
    }
}

fun playSong(context: Context,  songPath: File): ExoPlayer {
    // Create a SimpleExoPlayer instance
    val player = ExoPlayer.Builder(context).build()
    // Set the media source to the audio file
    val mediaItem = MediaItem.fromUri(songPath.toUri())
    player.setMediaItem(mediaItem)
    // Prepare the player for playback
    player.prepare()
    return player
}



