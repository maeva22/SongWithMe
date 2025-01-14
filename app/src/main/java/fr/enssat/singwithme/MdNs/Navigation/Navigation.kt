package fr.enssat.singwithme.MdNs.Navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.Navigation
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import fr.enssat.singwithme.MdNs.ErrorConnexion
import fr.enssat.singwithme.MdNs.ListMusic
import fr.enssat.singwithme.MdNs.LyricsScreen
import fr.enssat.singwithme.MdNs.SongsList

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
            SongsList(songs = songs, navController = navController)
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