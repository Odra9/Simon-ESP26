package it.unipd.dei.sivorleon.simon

import android.net.Uri
import android.os.Bundle
import android.util.Log
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
    companion object {
        var gameHistory : MutableList<Map<String, Any>> = mutableListOf()

        fun saveGame(game: Map<String, Any>) {
            gameHistory.add(game)
            Log.d(null, gameHistory.toString())
        }
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            SimonTheme {
                val navController = rememberNavController()

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavHost(
                        navController = navController, startDestination = "Data",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable("Game") {
                            Game()
                        }
                        composable("Data") {
                            MatchData(
                                data = gameHistory,
                                onClickLine = { game ->
                                    navController.navigate("Inspect/${Uri.encode(game)}")
                                }
                            )
                        }
                        composable("Inspect/{game}") { backStackEntry ->
                            MatchInspect(
                                game = Uri.decode(backStackEntry.arguments?.getString("game"))
                            )
                        }
                    }
                }
            }
        }
    }
}