package it.unipd.dei.sivorleon.simon.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import it.unipd.dei.sivorleon.simon.data.Game

/**
 * Displays a single game from the database
 *
 * @param[game] Game to inspect
 */
@Composable
fun MatchInspect(game: Game) {
    MatchInspectGameLine(
        game
    )
}

@Preview(showBackground = true)
@Composable
fun MatchInspectPreview() {
    MatchInspect(Game(0,"X".repeat(80), 10))
}