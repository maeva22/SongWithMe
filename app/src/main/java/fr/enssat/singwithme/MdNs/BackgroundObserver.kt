package fr.enssat.singwithme.MdNs

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.media3.exoplayer.ExoPlayer

class BackgroundObserver(private val exoPlayer: ExoPlayer) : LifecycleObserver {

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {
        // Arrêter la lecture lorsque l'application passe en arrière-plan
        if (exoPlayer.isPlaying) {
            exoPlayer.pause()
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop() {
        // Libérer les ressources d'ExoPlayer lorsque l'application n'est plus visible
        exoPlayer.stop()
        exoPlayer.release()
    }
}