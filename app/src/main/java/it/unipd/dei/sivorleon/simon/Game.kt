package it.unipd.dei.sivorleon.simon

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

@Composable
fun Game(test : () -> Unit) {
    Button(onClick = test) {
        Text(text = "ciao")
    }
}