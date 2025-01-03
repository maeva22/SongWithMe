package fr.enssat.singwithme.MdNs


import android.net.Uri
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController


@Composable
fun SongsList(
    modifier: Modifier = Modifier,
    songs: List<ListMusic>,
    navController: NavController
){
    Box {
        Text(
            modifier = modifier.padding(vertical = 40.dp, horizontal = 25.dp),
            text = "SongWithMe",
            color = Color.Black,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold
        )
        LazyColumn(modifier = modifier.padding(vertical = 100.dp, horizontal = 25.dp)) {
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
        modifier = modifier
            .padding(vertical = 4.dp, horizontal = 8.dp)
            .clickable {
                if (song.path == null){
                    navController.navigate("lyrics/${Uri.encode("null")}")
                }else {
                    song.path?.let { path ->
                        navController.navigate("lyrics/${Uri.encode(path)}")
                    }
                }
            }
    ) {

            CardContent(song)

        }
    }


@Composable
fun CardContent(song: ListMusic) {
    Row (modifier = Modifier
        .padding(24.dp)
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
                .padding(12.dp)
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

/*
    //"{ 0:19 }A better place to play?{ 0:21 }";
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SongWithMeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) {
                    KaraokeSimpleTextAnimate(
                        30000,
                        "What the hell i'm doing here"
                    )
                }
            }
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
        Surface(color=Color.Magenta,  modifier = Modifier.width(((width+15).pxToDp())).height(height.pxToDp()).padding(start = width.pxToDp()*progress,end=width.pxToDp()-(width.pxToDp()*progress))){}
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
*/