package it.unipd.dei.sivorleon.simon

import android.content.res.Configuration
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
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.unit.dp
import it.unipd.dei.sivorleon.simon.ui.theme.Blue
import it.unipd.dei.sivorleon.simon.ui.theme.Cyan
import it.unipd.dei.sivorleon.simon.ui.theme.Green
import it.unipd.dei.sivorleon.simon.ui.theme.Magenta
import it.unipd.dei.sivorleon.simon.ui.theme.Red
import it.unipd.dei.sivorleon.simon.ui.theme.Yellow

@Composable
fun Game(onEndGame: (String) -> Unit) {
    val orientation = LocalConfiguration.current.orientation

    var currGame by rememberSaveable { mutableStateOf("") }

    fun addToGame(code: String) {
        if (currGame == "") {
            currGame = code
        } else {
            currGame += ", $code"
        }
    }

    @Composable
    fun ColorElement(color: Color, code: String) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(color)
                .clickable { addToGame(code) }
        )
    }

    @Composable
    fun ColorGrid(modCol: Modifier) {
        Column(
            modifier = modCol,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                ColorElement(Red, "R")
                ColorElement(Green, "G")
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                ColorElement(Blue, "B")
                ColorElement(Magenta, "M")
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                ColorElement(Yellow, "Y")
                ColorElement(Cyan, "C")
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
                value = currGame,
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
                    onClick = { currGame = ""},
                    Modifier.width(128.dp)
                ) {
                    Text(text = "Cancella")
                }

                Button(
                    onClick = { onEndGame(currGame); currGame = "" },
                    Modifier.width(128.dp)
                ) {
                    Text(text = "Fine Partita")
                }
            }
        }
    }

    if (orientation == Configuration.ORIENTATION_PORTRAIT) {
        Column {
            ColorGrid(Modifier.fillMaxWidth().fillMaxHeight(0.6f).padding(top = 64.dp))

            TextButtons()
        }
    } else {
        Row {
            ColorGrid(Modifier.fillMaxWidth(0.5f).fillMaxHeight())

            TextButtons()
        }
    }
}