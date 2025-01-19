package fr.enssat.singwithme.MdNs.Back

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.FileOutputStream


data class ListMusic(
    val name: String,
    val artist: String,
    val path: String?,
    val locked: String?
)

/*
 * SONGS .JSON
 */
const val LOCAL_FILE_NAME = "playlist.json"

// Télécharger le JSON depuis une URL
fun downloadJson(url: String): String? {
    val client = OkHttpClient()
    val request = Request.Builder().url(url).build()
    val response = client.newCall(request).execute()
    return if (response.isSuccessful) response.body?.string() else null
}

// Sauvegarder le JSON localement
fun saveJson(context: Context, jsonString: String) {
    val ficherSave = File(context.cacheDir, LOCAL_FILE_NAME)
    FileOutputStream(ficherSave).use { it.write(jsonString.toByteArray()) }
}

// Charger le JSON local
fun loadJson(context: Context): String? {
    val ficherSave = File(context.cacheDir, LOCAL_FILE_NAME)
    return if (ficherSave.exists()) ficherSave.readText() else null
}

// Télécharger et mettre à jour le JSON local
fun downloadAndUpdateJson(context: Context, url: String): Boolean {
    val localData = loadJson(context)
    val remoteData = downloadJson(url)
    if (remoteData != null && remoteData != localData) {
        saveJson(context, remoteData)
        return true
    }
    return false
}

// Fonction pour parser le JSON
fun parseJson(jsonString: String): List<ListMusic> {
    val gson = Gson()
    val listType = object : TypeToken<List<ListMusic>>() {}.type
    return gson.fromJson(jsonString, listType)
}

