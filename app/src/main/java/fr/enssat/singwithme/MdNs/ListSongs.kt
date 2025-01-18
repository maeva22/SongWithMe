package fr.enssat.singwithme.MdNs

import android.net.Uri
import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController


@Composable
fun SongsList(
    modifier: Modifier = Modifier,
    initialSongs: List<ListMusic>,
    navController: NavController
){
    var songs by remember { mutableStateOf(initialSongs) }
    val context = LocalContext.current

    Column(modifier = modifier.fillMaxSize())  {
        Text(
            modifier = modifier.padding(vertical = 40.dp, horizontal = 25.dp)
                .align(Alignment.CenterHorizontally),
            text = "SongWithMe",
            color = Color.Black,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold
        )
        // Bouton de mise à jour de la liste
        Button(
            onClick = {
                Thread {
                    val isUpdated = downloadAndUpdateJson(context, JSON_URL)
                    val jsonData = loadJson(context)
                    songs = jsonData?.let { parseJson(it) } ?: emptyList()
                    if (isUpdated) {
                        Log.d("DownloadSong", "Playlist mise à jour : $songs")
                    }
                }.start() },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 16.dp), // Espacement entre le bouton et la liste
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Text(
                text = "Mettre à jour la liste",
                color = Color.White,
                style = MaterialTheme.typography.bodyLarge
            )
        }
        LazyColumn(modifier = modifier.padding( horizontal = 25.dp)) {
            items(songs) { song ->
                CardSong(
                    song = song,
                    navController = navController
                )
            }
        }
    }
}

// card for each song
@Composable
private fun CardSong(song: ListMusic, modifier: Modifier = Modifier, navController: NavController) {
    Card (
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        onClick = {
            if (song.path == null){
                navController.navigate("lyrics/${Uri.encode("null")}")
            }else {
                    navController.navigate("lyrics/${Uri.encode(song.path)}")
            }
        },
        modifier = modifier.padding(vertical = 4.dp, horizontal = 8.dp)
    ) {
            CardContent(song)
        }
    }


@Composable
fun CardContent(song: ListMusic) {
    Row (modifier = Modifier
        .padding(10.dp)
        .animateContentSize(
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            )
        )
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(5.dp)
        ) {
            Text(
                text = song.name,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "Artiste: ${song.artist}",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
