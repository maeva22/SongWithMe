package fr.enssat.singwithme.MdNs

import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
            if (karaoke == null || karaoke.isEmpty()) {
                Text(
                    text = "Impossible de charger les paroles.",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(16.dp)
                )
            } else {
                        val textSong = parseMd(karaoke)
                        val text = tabToString(textSong)
                        val parole = transformToData(textSong)
                        for (line in parole){
                            KaraokeSimpleTextAnimate(
                                duration = line.timerEnd - line.timerStart,
                                text = line.txt
                            )
                        }
                }
            }
        } else {
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

@Composable
fun Int.pxToDp() = with(LocalDensity.current) { this@pxToDp.toDp() }

@Composable
fun KaraokeSimpleText(text: String, progress: Float) {
    var width by remember { mutableIntStateOf(0) }
    var height by remember { mutableIntStateOf(0) }

    Box(Modifier.padding(Dp(50f))){
        Text(
            text=text,
            color = Color.Red,
            modifier = Modifier.onSizeChanged { width = it.width
                                                height = it.height},
            textAlign = TextAlign.Left
        )
        Surface(color=Color.Magenta,  modifier = Modifier.width(((width+15).pxToDp()))
            .height(height.pxToDp())
            .padding(start = width.pxToDp()*progress,end=width.pxToDp()-(width.pxToDp()*progress))){}
        Text(
            text=text,
            color = Color.White,
            modifier = Modifier.width(width.pxToDp()*progress),
            textAlign = TextAlign.Left,
            softWrap = false
        )

    }
    Log.d("LectureString","Width : $width , Progress : $progress")

}
@Composable
fun KaraokeSimpleTextAnimate(duration: Int, text: String)  {
    val karaokeAnimation = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        karaokeAnimation.animateTo(1f, tween(duration, easing = LinearEasing))
    }
    Log.d("LectureString","Animation : ${karaokeAnimation.value}")
    KaraokeSimpleText(text, karaokeAnimation.value)
}

