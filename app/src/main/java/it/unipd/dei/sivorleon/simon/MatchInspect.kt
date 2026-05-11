package it.unipd.dei.sivorleon.simon

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun MatchInspect(game: Map<String, Any>) {
    MatchInspectGameLine(
        game
    )
}

@Preview(showBackground = true)
@Composable
fun MatchInspectPreview() {
    MatchInspect(mapOf("max" to "---------------------------very long game---------------------------------------", "errorPos" to 10))
}