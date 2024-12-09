package fr.enssat.singwithme.MdNs

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.net.URL

const val lien = "https://gcpa-enssat-24-25.s3.eu-west-3.amazonaws.com/"

// Create a data class to define the class containing the variables
data class ListMusic(
    val name: String,
    val artist: String,
    val path: String,
    val locked: Boolean
)

private fun parseJSON(jsonString: String) {
    // Define the Gson Object
    val gson = Gson()
    val listType = object : TypeToken<List<ListMusic>>() {}.type
    val musicList: List<ListMusic> = gson.fromJson(jsonString, listType)
    // Afficher la liste des musiques
    for (music in musicList) {
        println("name: ${music.name}")
        println("artist: ${music.artist}")
        println("locked: ${music.locked}")
        println("path: ${music.path}")
        if(music.path != null){
            parseMd(lien+music.path)
        }
    }
}

data class Karaoke(
    val lyrics: List<LyricLine>
)

// Classe pour représenter une ligne de paroles
data class LyricLine(
    val timestamp: String, // Minutage { mm:ss }
    val text: String       // Texte de la ligne
)

fun parseMd(MdString: String) {
    // Lire le fichier Markdown
    val markdown = URL(MdString).readText().lines()

    val lyrics = mutableListOf<LyricLine>()

    // Variable pour détecter la section actuelle
    var currentSection = ""

    // Parcourir chaque ligne
    for (line in markdown) {
        when {
            line.startsWith("# lyrics") -> {
                currentSection = "lyrics"
            }
            currentSection == "lyrics" && line.contains(Regex("""\{\s*\d+:\d+\s*\}""")) -> {
                // Extraire les minutages et le texte
                val regex = Regex("""\s*(\d+:\d+)\s*(.*)""")
                val match = regex.find(line)
                if (match != null) {
                    val timestamp = match.groupValues[1].trim()
                    val text = match.groupValues[2].trim()
                    lyrics.add(LyricLine(timestamp,text))
                }
            }
        }
    }

    // Créer l'objet Song
    val song = Karaoke(lyrics)

    // Afficher les résultats
    println("Paroles :")
    song.lyrics.forEach { println("${it.timestamp} -> ${it.text}") }
}

fun main() {
    // Define the JSON String
    val musicsJson = URL(lien+"playlist.json").readText()
    // Call the parseJSON function
    parseJSON(musicsJson)
}