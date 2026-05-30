package it.unipd.dei.sivorleon.simon.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import it.unipd.dei.sivorleon.simon.data.Game

/**
 * The list of Games UI
 *
 * @param[data] the entire gameHistory
 * @param[onClickLine] navController function to be called when a game is inspected. Navigates to MatchInspect composable
 * @param[onClickFAB] navController function to be called when FAB is clicked. Navigates to Game composable
 */
@Composable
fun MatchData(data: MutableList<Game>?, onClickLine: (Int) -> Unit, onClickFAB: () -> Unit) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = onClickFAB
            ) {
                Icon(Icons.Filled.Add, "Go to Game Screen")
            }
        }
    ) { innerPadding ->
        LazyColumn (
            modifier = Modifier.padding(innerPadding).fillMaxSize()
        ) {
            items(data!!.size) {
                MatchDataGameLine (
                    data[it],
                    it,
                    onClickLine
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ClickableGameLinePreview() {
    MatchData(
        mutableListOf(Game(0,"---------------------------very long game---------------------------------------", 10)),
        {},
        {}
    )
}