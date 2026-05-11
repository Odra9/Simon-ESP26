package it.unipd.dei.sivorleon.simon

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import it.unipd.dei.sivorleon.simon.ui.theme.Red

private const val NUM_WEIGHT = 0.10f //How much width do the numbers occupy percentage wise

@Composable
fun GameLine(game: Map<String, Any>, onClick: (Map<String, Any>) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().clickable(onClick = { onClick(game) })
    ) {
        //Number
        Text(
            text = game["max"].toString().length.toString(),
            modifier = Modifier.weight(NUM_WEIGHT)
        )
        //Text
        Row(
            modifier = Modifier.weight(1 - NUM_WEIGHT)
        ) {
            Text(
                text = game["max"].toString(),
                overflow = TextOverflow.Ellipsis,
                softWrap = false
            )

            Text(
                text = game["max"].toString(),
                color = Red,
                overflow = TextOverflow.Ellipsis,
                softWrap = false
            )
        }

    }
}

@Preview(showBackground = true)
@Composable
fun GameLinePreview() {
    GameLine(mapOf("max" to "---------------------------very long game---------------------------------------", "errorPos" to 10)) {}
}

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
                GameLine(data[it], onClickLine)
            }
        }
    }
}