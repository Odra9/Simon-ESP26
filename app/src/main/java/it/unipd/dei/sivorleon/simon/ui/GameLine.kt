package it.unipd.dei.sivorleon.simon.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import it.unipd.dei.sivorleon.simon.data.Game
import it.unipd.dei.sivorleon.simon.ui.theme.Red

/**
 * This composable displays a single game from the database
 * It gets called with different, personalized params by MatchData and MatchInspect
 *
 * @param[game] Game to be displayed on this line
 * @param[modifier] Row modifier
 * @param[numWeight] Percentage of horizontal space occupied by the max length of correct sequence
 * @param[textSize] Text font size
 * @param[overflow] How should overflowing text appear
 * @param[softWrap] Should the text wrap around soft line breaks
 */
@Composable
fun GameLine(
    game: Game,
    modifier: Modifier,
    numWeight: Float,
    textSize: TextUnit,
    overflow: TextOverflow,
    softWrap: Boolean
) {
    val length = game.sequence.length

    // The text must display through different colors where the user made the mistake
    val blackText = game.sequence.slice(IntRange(0, (game.errorPos - 1)))
    val redText = game.sequence.slice(IntRange(game.errorPos, length - 1))

    val annotatedText = buildAnnotatedString {
        append(blackText)
        withStyle(style = SpanStyle(color = Red)) {
            append(redText)
        }
    }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.Top
    ) {
        //Number
        Text(
            text = (length - 1).toString(), // The max length of sequence the user correctly played is always 1 less of the current sequence length
            modifier = Modifier.weight(numWeight),
            fontSize = textSize
        )
        //Text
        Text(
            text = annotatedText,
            modifier = Modifier.weight(1 - numWeight),
            fontSize = textSize,
            overflow = overflow,
            softWrap = softWrap
        )
    }
}

@Composable
fun MatchDataGameLine(game: Game, gameIndex: Int, onClick: (Int) -> Unit) {
    GameLine(
        game,
        Modifier.fillMaxWidth().clickable(onClick = { onClick(gameIndex) }),
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
        0.15f,
        28.sp,
        TextOverflow.Clip,
        true
    )
}