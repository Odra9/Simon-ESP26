package it.unipd.dei.sivorleon.simon

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp

@Composable
fun MatchInspect(game: String) {
    val NUM_WEIGHT = 0.08f //How much width do the numbers occupy percentage wise

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        //Numbers
        Text(
            text = (game.length/3 + 1).toString(),
            modifier = Modifier.weight(NUM_WEIGHT),
            fontSize = 16.sp
        )
        //Games
        Text(
            text = game,
            modifier = Modifier.weight(1 - NUM_WEIGHT),
            fontSize = 24.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MatchInspectPreview() {
    // Pass your custom parameter here
    MatchInspect(game = "---------------------------very long game---------------------------------------")
}