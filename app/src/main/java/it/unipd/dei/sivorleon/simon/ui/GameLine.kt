package it.unipd.dei.sivorleon.simon.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import it.unipd.dei.sivorleon.simon.data.Game
import it.unipd.dei.sivorleon.simon.ui.theme.Red

@Composable
fun GameLine(
    game: Game,
    modifier: Modifier,
    verticalAlignment: Alignment.Vertical,
    numWeight: Float,
    textSize: TextUnit,
    overflow: TextOverflow,
    softWrap: Boolean
) {
    val length = game.sequence.length

    val blackText = game.sequence.slice(IntRange(0, (game.errorPos - 1)))
    val redText = game.sequence.slice(IntRange(game.errorPos, length - 1))

    Row(
        modifier = modifier,
        verticalAlignment = verticalAlignment
    ) {
        //Number
        Text(
            text = length.toString(),
            modifier = Modifier.weight(numWeight),
            fontSize = textSize
        )
        //Text
        Row(
            modifier = Modifier.weight(1 - numWeight)
        ) {
            Text(
                text = blackText,
                fontSize = textSize,
                overflow = overflow,
                softWrap = softWrap
            )

            Text(
                color = Red,
                text = redText,
                fontSize = textSize,
                overflow = overflow,
                softWrap = softWrap
            )
        }
    }
}

@Composable
fun MatchDataGameLine(game: Game, gameIndex: Int, onClick: (Int) -> Unit) {
    GameLine(
        game,
        Modifier.fillMaxWidth().clickable(onClick = { onClick(gameIndex) }),
        Alignment.Top,
        0.1f,
        20.sp,
        TextOverflow.Ellipsis,
        false
    )
}

@Composable
fun MatchInspectGameLine(game: Game) {
    GameLine(
        game,
        Modifier.fillMaxSize(),
        Alignment.Top,
        0.15f,
        28.sp,
        TextOverflow.Clip,
        true
    )
}