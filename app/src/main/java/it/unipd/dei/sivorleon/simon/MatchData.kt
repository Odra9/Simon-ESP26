package it.unipd.dei.sivorleon.simon

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow

@Composable
fun GameLine(game: String) {
    val numbersWeight = 0.10f

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
        modifier = Modifier.fillMaxWidth()
    ) {
        //Numbers
        Text(
            text = length.toString(),
            modifier = Modifier.weight(numbersWeight)
        )
        //Games
        Text(
            text = text,
            modifier = Modifier.weight(1 - numbersWeight),
            overflow = TextOverflow.Ellipsis,
            softWrap = false
        )
    }
}

@Composable
fun MatchData(data: String) {
    val lines = data.lines()

    LazyColumn (
        modifier = Modifier.fillMaxSize()
    ) {
        items(lines.size - 1) { //The last element in 'lines' if an EOF
            GameLine(lines[it])
        }
    }
}