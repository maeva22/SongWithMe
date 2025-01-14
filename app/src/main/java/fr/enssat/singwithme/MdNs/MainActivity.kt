package fr.enssat.singwithme.MdNs

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import fr.enssat.singwithme.MdNs.Navigation.AppNavigation
import fr.enssat.singwithme.MdNs.ui.theme.SongWithMeTheme

const val JSON_URL = "https://gcpa-enssat-24-25.s3.eu-west-3.amazonaws.com/playlist.json"

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Thread {
            // si fichier de sauvegarde present
            var jsonData = loadJson(this)
            var songs = jsonData?.let { parseJson(it) } ?: emptyList()
            if (songs.isNotEmpty()) {
                runOnUiThread {
                    setContent {
                        SongWithMeTheme {
                            AppNavigation(songs = songs)
                        }
                    }
                }
            }else if(!checkInternet(this)){
                // check internet
                runOnUiThread {
                    setContent {
                        SongWithMeTheme {
                            ErrorConnexion()
                        }
                    }
                }
            }else{
                // Mise à jour de notre liste de musiques
                val isUpdated = downloadAndUpdateJson(this, JSON_URL)
                jsonData = loadJson(this)
                songs = jsonData?.let { parseJson(it) } ?: emptyList()
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
            }

        }.start()
    }

    private fun checkInternet(context: Context): Boolean {
        val connectivityManager = context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager

        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false

        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
    }
}
