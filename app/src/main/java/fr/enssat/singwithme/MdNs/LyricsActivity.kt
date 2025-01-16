package fr.enssat.singwithme.MdNs

import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.delay


const val BASE_URL = "https://gcpa-enssat-24-25.s3.eu-west-3.amazonaws.com/"

@Composable
fun LyricsScreen(songPath: String?, navController: NavController) {
    val context = LocalContext.current

    if (songPath != "null") {
        val mdContent = dowloadMd(context, songPath.toString())
        val karaoke = mdContent?.let { parseMd(it) }
        val launchMusic = remember { mutableStateOf(false) }
        // Affichage de l'écran
        Column(modifier = Modifier.padding(16.dp)) {
            // Bouton pour revenir à la liste des chansons
            Button(
                modifier = Modifier.padding(bottom = 16.dp),
                onClick = { navController.navigateUp() }
            ) {
                Text(text = "Retour")
            }
            ElevatedButton(
                onClick = { launchMusic.value = !launchMusic.value }
            ) {
                Text(if (launchMusic.value) "Stop" else "Play")
            }

            if(launchMusic.value){
                if (karaoke.isNullOrEmpty()) {
                    Text(
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyMedium,
                        text = "Impossible de charger les paroles."
                    )
                } else {

                    // GET the data to good format
                    val textSong = parseMd(karaoke)
                    val parole = transformToData(textSong)

                    KarokeBox(parole)
                }
            }
        }
    } else {
        Column(modifier = Modifier.padding(16.dp)) {
            // Bouton pour revenir à la liste des chansons
            Button(
                modifier = Modifier.padding(bottom = 16.dp),
                onClick = { navController.navigateUp() }
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

@Composable
fun Int.pxToDp() = with(LocalDensity.current) { this@pxToDp.toDp() }

@Composable
fun KaraokeSimpleText(text: String, progress: Float) {
    var width by remember { mutableIntStateOf(0) }
    var height by remember { mutableIntStateOf(0) }


    Box(Modifier.padding(Dp(50f))) {
        Text(
            text = text,
            color = Color.Red,
            modifier = Modifier.onSizeChanged {
                width = it.width
                height = it.height
            },
            textAlign = TextAlign.Left
        )

        Surface(
            color = Color.Magenta, modifier = Modifier
                .width(((width + 15).pxToDp()))
                .height(height.pxToDp())
                .padding(
                    start = width.pxToDp() * progress,
                    end = width.pxToDp() - (width.pxToDp() * progress)
                )
        ) {}

        Text(
            text = text,
            color = Color.White,
            modifier = Modifier.width(width.pxToDp() * progress),
            textAlign = TextAlign.Left,
            softWrap = false
        )

    }
    Log.d("LectureString", "Width : $width , Progress : $progress")

}

@Composable
fun KaraokeSimpleTextAnimate(duration: Int, text: String,key: Int) {
    val karaokeAnimation = remember { Animatable(0f) }

    LaunchedEffect(key) {
        karaokeAnimation.snapTo(0f)

        karaokeAnimation.animateTo(1f, tween(duration, easing = LinearEasing))
    }
    Log.d("LectureString", "Animation : ${karaokeAnimation.value}")
    KaraokeSimpleText(text, karaokeAnimation.value)
}
@Composable
fun KarokeBox(parole: List<ParoleParse>){

    var indexString by remember { mutableStateOf(1) }
    val line = parole[indexString]
    val text = line.txt
    val duration =  line.timerEnd - line.timerStart

    LaunchedEffect(indexString) {
        if (indexString < parole.size) {
            delay(duration.toLong())  // Attendre la durée de l'animation
            indexString += 1  // Incrémenter l'indice pour le texte suivant
        }
    }
    KaraokeSimpleTextAnimate(duration,text,indexString)
}

