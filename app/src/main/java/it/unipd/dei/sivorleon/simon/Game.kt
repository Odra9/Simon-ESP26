package it.unipd.dei.sivorleon.simon

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import it.unipd.dei.sivorleon.simon.ui.theme.Green
import it.unipd.dei.sivorleon.simon.ui.theme.Red

@Composable
fun Game(onEndGame: (String) -> Unit) {
    var currGame by rememberSaveable { mutableStateOf("") }

    fun addToGame(code: String) {
        if (currGame == "") {
            currGame = code
        } else {
            currGame += ", $code"
        }
    }

    Column() {
        Column() {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Box(
                    modifier = Modifier
                        .size(width = 100.dp, height = 100.dp)
                        .background(Red)
                        .clickable { addToGame("R") }
                )
                Box(
                    modifier = Modifier
                        .size(width = 100.dp, height = 100.dp)
                        .background(Green)
                        .clickable { addToGame("G") }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        Spacer(modifier = Modifier.height(32.dp))

        TextField(
            value = currGame,
            onValueChange = {},
            enabled = false,
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp, 0.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = { currGame = "" }) {
                Text(text = "Cancella")
            }

            Button(onClick = { onEndGame(currGame); currGame = "" }) {
                Text(text = "Fine Partita")
            }
        }
    }
}