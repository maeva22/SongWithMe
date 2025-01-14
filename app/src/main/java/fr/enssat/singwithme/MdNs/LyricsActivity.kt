package fr.enssat.singwithme.MdNs

import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


const val BASE_URL = "https://gcpa-enssat-24-25.s3.eu-west-3.amazonaws.com/"

@Composable
fun LyricsScreen(songPath: String?, navController: NavController) {
    val context = LocalContext.current

    if (songPath != "null" ){
        val mdContent = dowloadMd(context, songPath.toString())
        val karaoke = mdContent?.let { parseMd(it) }

        // Affichage de l'écran
        Column(modifier = Modifier.padding(16.dp)) {
            // Bouton pour revenir à la liste des chansons
            Button(
                onClick = { navController.navigateUp() },
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Text(text = "Retour")
            }
            if (karaoke == null || karaoke.lyrics.isEmpty()) {
                Text(
                    text = "Impossible de charger les paroles.",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(16.dp)
                )
            } else {
                LazyColumn(modifier = Modifier.padding(16.dp)) {
                    items(karaoke.lyrics) { lyricLine ->
                        Text(
                            text = lyricLine.text,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }
                }
            }
        }

    }else {
        Column(modifier = Modifier.padding(16.dp)) {
            // Bouton pour revenir à la liste des chansons
            Button(
                onClick = { navController.navigateUp() },
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Text(text = "Retour")
            }
            Text(
                text = "Les paroles sont actuellement indisponible !",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}
