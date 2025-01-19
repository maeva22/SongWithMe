package fr.enssat.singwithme.MdNs.Front

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ErrorConnexion(
    modifier: Modifier = Modifier,
){
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Vous devez être connecté à internet pour le premier lancement !",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(16.dp)
        )
    }
}