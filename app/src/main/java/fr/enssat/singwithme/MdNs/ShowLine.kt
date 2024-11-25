package fr.enssat.singwithme.MdNs


import android.annotation.SuppressLint
import android.icu.text.ListFormatter.Width
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.toSize
import fr.enssat.singwithme.MdNs.ui.theme.SongWithMeTheme


class ShowLine : ComponentActivity() {

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
