package fr.enssat.singwithme.MdNs

import android.app.Activity
import android.content.pm.ActivityInfo
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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.media3.exoplayer.ExoPlayer
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import java.io.File


const val BASE_URL = "https://gcpa-enssat-24-25.s3.eu-west-3.amazonaws.com/"

@Composable
fun LyricsScreen(songPath: String?, navController: NavController) {
    val activity = LocalContext.current as Activity
    val context = LocalContext.current


    // Forcer l'orientation paysage uniquement pour ce composable
    LaunchedEffect(Unit) {
        activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
    }

    // Réinitialiser l'orientation quand on quitte cette page
    DisposableEffect(Unit) {
        onDispose {
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        }
    }


    if (songPath != "null") {
        val mdContent = dowloadMd(context, songPath.toString())
        val karaoke = mdContent?.let { parseMd(it) }
        val launchMusic = rememberSaveable  { mutableStateOf(false) }
        var mP3File by remember { mutableStateOf<File?>(null) }
        var exoPlayer by remember { mutableStateOf<ExoPlayer?>(null) }


        // Télécharger le fichier MP3 si nécessaire
        LaunchedEffect(songPath) {
            mP3File = songPath?.let { downloadSong(context, it) }
        }

        // Libérer le lecteur lorsqu'il n'est plus nécessaire
        DisposableEffect(Unit) {
            onDispose {
                exoPlayer?.release()
            }
        }

        // Affichage de l'écran
        Column(modifier = Modifier.padding(16.dp)) {
            // Ligne contenant les boutons alignés
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp), // Ajout de l'espacement en bas
                horizontalArrangement = Arrangement.SpaceBetween // Alignement des boutons
            ) {
                // Bouton "Retour" aligné à gauche
                Button(onClick = { navController.navigateUp() }) {
                    Text(text = "Retour")
                }

                // Bouton "Play/Stop" aligné à droite
                ElevatedButton(onClick = {
                    launchMusic.value = !launchMusic.value
                    if (launchMusic.value) {
                        // Initialiser ExoPlayer
                        exoPlayer = mP3File?.let { playSong(context, it) }
                    } else {
                        exoPlayer?.stop()
                    }}) {
                    Text(if (launchMusic.value) "Stop" else "Play")
                }
            }

            // Si la musique est en cours de lecture
            if (launchMusic.value) {
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

                    KarokeBox(parole, exoPlayer)
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

    Box(
        modifier = Modifier
            .fillMaxSize() // Remplit tout l'écran
            .padding(5.dp), // Ajoute un padding autour de la Box
        contentAlignment = Alignment.Center

    ) {
        Box(
            modifier = Modifier
                .wrapContentSize()
        ) {
            Text(
                text = text,
                color = Color.Red,
                softWrap = false,
                modifier = Modifier.onSizeChanged {
                    width = it.width
                    height = it.height
                },
                textAlign = TextAlign.Left,
                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 30.sp) // Taille du texte de 30 sp

                )

            Surface(
                color = Color.Magenta, modifier = Modifier
                    .width(((width + 15).pxToDp()))
                    .height(height.pxToDp())
                    .padding(
                        start = width.pxToDp() * progress,
                        end = width.pxToDp() - (width.pxToDp() * progress),
                        //top= 152.pxToDp(),
                        //bottom = 0.pxToDp()
                    )
            ) {}

            Text(
                text = text,
                color = Color.Blue,
                modifier = Modifier.width(width.pxToDp() * progress),
                textAlign = TextAlign.Left,
                softWrap = false ,
                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 30.sp)
            )

        }
    }

}

@Composable
fun KaraokeSimpleTextAnimate(Dataline: ParoleParse, indice: Int, exoPlayer: ExoPlayer?) {

    val karaokeAnimation = remember  { Animatable(0f) }
    var TargetValue by rememberSaveable  { mutableFloatStateOf(0f) }
    var savedValue by rememberSaveable  { mutableFloatStateOf(0f) }
    val text = Dataline.totalText

    LaunchedEffect(Dataline,indice) { // Valeur différente a chaque appel pour "reset le truic ! "
        if(Dataline != ParoleParse()){
            if(indice == 0){
                karaokeAnimation.snapTo(0f)
                TargetValue=0f
                if (exoPlayer != null) {
                    delay(Dataline.timerStart.first()-exoPlayer.currentPosition)
                }

            }
            val duration = Dataline.timerEnd[indice] - Dataline.timerStart[indice]
            var len = (Dataline.txt[indice].length).toFloat()
            TargetValue += ((Dataline.txt[indice].length).toFloat() / (Dataline.totalLetter).toFloat())

            if(TargetValue!=1f){
                Log.d("verif","oh")
            }

            karaokeAnimation.animateTo(TargetValue, tween(duration, easing = LinearEasing))

            Log.d("LectureString", "-----------------------------------")
            Log.d("LectureString", "TargetValue : ${TargetValue}")
            Log.d("LectureString", "len : ${len}")
            Log.d("LectureString", "indice : ${indice}")
            Log.d("LectureString", "text : ${text}")

        }

    }
   savedValue = karaokeAnimation.value
   // Log.d("LectureString", "Animation : ${karaokeAnimation.value}")
    KaraokeSimpleText(text, savedValue)
}
@Composable
fun KarokeBox(parole: List<ParoleParse>, exoPlayer: ExoPlayer?){

    var indexString by rememberSaveable  { mutableIntStateOf(0) }
    var indice by rememberSaveable  { mutableIntStateOf(0) }
    var timerSong by rememberSaveable { mutableLongStateOf(0) }

    val DataLine = parole[indexString]

    LaunchedEffect(indexString,indice) {
            if (indexString < parole.size-1) {

                val duration = DataLine.timerEnd[indice] - DataLine.timerStart[indice]
                if (exoPlayer != null) {
                    timerSong = exoPlayer.currentPosition
                }
                delay(parole[indexString].timerEnd[indice] - timerSong)// Attendre la durée de l'animation

                if( indice < DataLine.txt.size-1 ){
                    indice+=1
                }else{

                    indexString += 1  // Incrémenter l'indice pour le texte suivant
                    indice =0
                }

            }


    }

    KaraokeSimpleTextAnimate(DataLine,indice,exoPlayer)


}

