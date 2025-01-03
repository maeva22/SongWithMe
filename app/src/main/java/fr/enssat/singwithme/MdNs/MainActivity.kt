package fr.enssat.singwithme.MdNs

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import fr.enssat.singwithme.MdNs.Navigation.AppNavigation
import fr.enssat.singwithme.MdNs.ui.theme.SongWithMeTheme

const val JSON_URL = "https://gcpa-enssat-24-25.s3.eu-west-3.amazonaws.com/playlist.json"

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Thread {
            // Mise à jour de notre liste de musiques
            val isUpdated = downloadAndUpdateJson(this, JSON_URL)
            val jsonData = loadJson(this)
            val songs = jsonData?.let { parseJson(it) } ?: emptyList()

            runOnUiThread {
                if (isUpdated) {
                    Toast.makeText(this, "Playlist mise à jour", Toast.LENGTH_SHORT).show()
                }
                setContent {
                    SongWithMeTheme {
                        AppNavigation(songs = songs)
                    }
                }
            }
        }.start()
    }
}
