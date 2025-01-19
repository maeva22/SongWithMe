package fr.enssat.singwithme.MdNs.Navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import fr.enssat.singwithme.MdNs.Front.ErrorConnexion
import fr.enssat.singwithme.MdNs.Back.ListMusic
import fr.enssat.singwithme.MdNs.Front.LyricsScreen
import fr.enssat.singwithme.MdNs.Front.SongsList

/**
 * Provides Navigation graph for the application.
 */

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    songs: List<ListMusic>
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "listSong",
        modifier = modifier
    ) {
        composable(route = "listSong") {
            SongsList(initialSongs = songs, navController = navController)
        }
        composable(
            route = "lyrics/{songPath}",
            arguments = listOf(
                navArgument("songPath") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val songPath = backStackEntry.arguments?.getString("songPath")
            LyricsScreen(songPath = songPath?: "", navController = navController)
        }
        composable(route = "ErrorPage") {
            ErrorConnexion()
        }
    }
}