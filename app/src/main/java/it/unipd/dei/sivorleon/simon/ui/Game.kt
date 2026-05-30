package it.unipd.dei.sivorleon.simon.ui

import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.EaseInOutQuint
import androidx.compose.animation.core.TweenSpec
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import it.unipd.dei.sivorleon.simon.GameController
import it.unipd.dei.sivorleon.simon.R
import it.unipd.dei.sivorleon.simon.tiles

@Composable
fun Game(onEndGame: () -> Unit) {
    val orientation = LocalConfiguration.current.orientation

    val controller = GameController.getController()

    var isStartGameEnabled by rememberSaveable { mutableStateOf(true) }

    @Composable
    fun ColorElement(index: Int) {
        val colorAnimation: Color by animateColorAsState(
            targetValue = if (!controller.colorStartAnimation(index)) tiles[index].color else Color.White,
            animationSpec = TweenSpec(
                durationMillis = controller.animationDuration.toInt()-50,
                easing = EaseInOutQuint
            ),
            label = "alpha", finishedListener = { controller.colorAnimationHasEnded(index) }
        )

        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(colorAnimation)
                .clickable(
                    enabled = controller.isGameActive,
                    onClick = { controller.tileClickHandler(index) }
                )
        )
    }

    @Composable
    fun ColorGrid(modCol: Modifier) {
        Column(
            modifier = modCol,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            repeat(3) { i ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    repeat(2) { j ->
                        ColorElement(2*i + j)
                    }
                }
            }
        }
    }

    @Composable
    fun TextButtons() {
        Column(
            modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            TextField(
                value = (
                    when (val t = controller.displayTextHandler()) {
                        "ERROR" -> stringResource(R.string.UserErrorDetected)
                        "END" -> stringResource(R.string.UserTerminatedGame)
                        else -> t
                    }
                ),
                onValueChange = {},
                enabled = false,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(48.dp, 16.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    enabled = isStartGameEnabled,
                    onClick = {
                        controller.startGame()
                        isStartGameEnabled = false
                    },
                    modifier = Modifier.weight(1f, false)
                ) {
                    Text(text = stringResource(R.string.StartButton))
                }

                Button(
                    enabled = controller.isSequenceBeingAnimated,
                    onClick = {
                        controller.pauseGame()
                    },
                    modifier = Modifier.weight(0.8f, false)
                ) {
                    Text(
                        text = (
                            if (controller.isGamePaused) {
                                stringResource(R.string.PauseButton_Continue)
                            } else {
                                stringResource(R.string.PauseButton)
                            }
                        )
                    )
                }

                Button(
                    enabled = controller.isGameActive,
                    onClick = {
                        controller.endGame()
                        onEndGame()
                    },
                    modifier = Modifier.weight(1f, false)
                ) {
                    Text(text = stringResource(R.string.EndButton))
                }
            }
        }
    }

    if (orientation == Configuration.ORIENTATION_PORTRAIT) {
        Column {
            ColorGrid(Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.6f)
                .padding(top = 64.dp))

            TextButtons()
        }
    } else {
        Row {
            ColorGrid(Modifier
                .fillMaxWidth(0.5f)
                .fillMaxHeight())

            TextButtons()
        }
    }

    BackHandler {
        controller.endGame()
        onEndGame()
    }
}