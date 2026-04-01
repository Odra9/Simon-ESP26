package it.unipd.dei.sivorleon.simon

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import it.unipd.dei.sivorleon.simon.ui.theme.Purple80
import it.unipd.dei.sivorleon.simon.ui.theme.PurpleGrey80

@Composable
fun Game(onCancel: () -> Unit, onEndGame: () -> Unit) {
    var num = 0
    Column() {
        Column() {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Box(
                    modifier = Modifier
                        .size(width = 100.dp, height = 100.dp)
                        .background(PurpleGrey80)
                        .clickable {
                            num += 1
                            Log.v(null, num.toString())
                        }
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Box(
                    modifier = Modifier.background(color = Purple80)
                )
                Box(
                    modifier = Modifier.background(color = Purple80)
                )
                Box(
                    modifier = Modifier.background(color = Purple80)
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(text = "List")

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = onCancel) {
                Text(text = "Cancella")
            }

            Button(onClick = onEndGame) {
                Text(text = "Fine Partita")
            }
        }
    }
}