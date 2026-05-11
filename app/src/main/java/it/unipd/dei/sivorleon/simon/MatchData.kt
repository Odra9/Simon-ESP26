package it.unipd.dei.sivorleon.simon

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

@Composable
fun MatchData(data: MutableList<Map<String, Any>>, onClickLine: (Map<String, Any>) -> Unit, onClickFAB: () -> Unit) {
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
            items(data.size) {
                MatchDataGameLine (
                    data[it],
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
        mutableListOf(mapOf("max" to "---------------------------very long game---------------------------------------", "errorPos" to 10)),
        {},
        {}
    )
}