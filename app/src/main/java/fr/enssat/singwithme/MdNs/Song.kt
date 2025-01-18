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
    if (mdPath.isNullOrEmpty()) return null

    val mp3Path = mdPath.replace(".md", ".mp3")
    val client = OkHttpClient()
    val request = Request.Builder().url(BASE_URL + mp3Path).build()
    val songFile = File(context.filesDir, mp3Path.substringAfterLast('/'))

    if (songFile.exists()) {
        Log.d("DownloadSong", "Fichier trouvé dans le répertoire : ${songFile.absolutePath}")
        return songFile
    }

    try {
        Thread {
            val response = client.newCall(request).execute()
            if (response.isSuccessful) {
                response.body?.byteStream()?.use { inputStream ->
                    songFile.outputStream().use { outputStream ->
                        inputStream.copyTo(outputStream)
                    }
                    Log.d("DownloadSong", "Fichier téléchargé et sauvegardé : ${songFile.absolutePath}")
                }
            } else {
                Log.e("DownloadSong", "Erreur lors du téléchargement : ${response.message}")
            }
        }.start()
        return songFile
    } catch (e: IOException) {
        Log.e("DownloadSong", "Exception lors du téléchargement : ${e.message}")
    }

    return null
}

fun playSong(context: Context,  songPath: File): ExoPlayer {
    // Create a SimpleExoPlayer instance
    val player = ExoPlayer.Builder(context).build()
    // Set the media source to the audio file
    val mediaItem = MediaItem.fromUri(songPath.toUri())
    player.setMediaItem(mediaItem)
    // Prepare the player for playback
    player.prepare()
    player.play()
    return player
}



