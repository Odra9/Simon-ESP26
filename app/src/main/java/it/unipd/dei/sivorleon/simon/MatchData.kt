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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow

private const val NUM_WEIGHT = 0.10f //How much width do the numbers occupy percentage wise

@Composable
fun GameLine(game: String, onClick: (String) -> Unit) {
    var length : Int
    var text : String

    if (game == "0") {
        length = 0
        text = stringResource(R.string.EmptyGame)
    } else {
        length = (game.length/3 + 1)  //converts character number into number of squares pressed
        text = game
    }

    Row(
        modifier = Modifier.fillMaxWidth().clickable(onClick = { onClick(text) })
    ) {
        //Numbers
        Text(
            text = length.toString(),
            modifier = Modifier.weight(NUM_WEIGHT)
        )
        //Games
        Text(
            text = text,
            modifier = Modifier.weight(1 - NUM_WEIGHT),
            overflow = TextOverflow.Ellipsis,
            softWrap = false
        )
    }
}

@Composable
fun MatchData(data: MutableList<Map<String, Any>>, onClickLine: (String) -> Unit, onClickFAB: () -> Unit) {
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
                GameLine(data[it]["max"].toString(), onClickLine)
            }
        }
    }
}