package it.unipd.dei.sivorleon.simon

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import it.unipd.dei.sivorleon.simon.ui.theme.SimonTheme

class MainActivity : ComponentActivity() {
    var gameHistory : String = ""
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState != null) {
            val strValue = savedInstanceState.getString("history")
            if (strValue != null) gameHistory = strValue
        }

        enableEdgeToEdge()

        setContent {
            SimonTheme {
                val navController = rememberNavController()

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavHost(
                        navController = navController, startDestination = "Game",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable("Game") {
                            Game(
                                onEndGame = {currGame ->
                                    saveGame(currGame)
                                    navController.navigate("Data")
                                }
                            )
                        }
                        composable("Data") {
                            MatchData(
                                data = gameHistory
                            )
                        }
                    }
                }
            }
        }
    }

    fun saveGame(game: String) {
        if (game == "") {
            gameHistory += "0\n"
        } else {
            gameHistory += game + "\n"
        }

    }

    override fun onSaveInstanceState(outState: Bundle)
    {
        super.onSaveInstanceState(outState)
        outState.putString("history", gameHistory)
    }
}