package it.unipd.dei.sivorleon.simon.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import it.unipd.dei.sivorleon.simon.data.Game

@Composable
fun MatchInspect(game: Game) {
    MatchInspectGameLine(
        game
    )
}

@Preview(showBackground = true)
@Composable
fun MatchInspectPreview() {
    MatchInspect(Game(0,"---------------------------very long game---------------------------------------", 10))
}