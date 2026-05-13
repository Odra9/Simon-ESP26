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
        var gameHistory : MutableList<Map<String, Any>> = mutableListOf(mapOf("max" to "AAAAAAAAAAAAAAAAA", "errorPos" to 5))

        fun saveGame(game: Map<String, Any>) {
            gameHistory.add(game)
        }
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //TEMPORARY SOLUTION
        var tmp_gamemap : Map<String, Any> = mapOf()

        enableEdgeToEdge()

        setContent {
            SimonTheme {
                val navController = rememberNavController()

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavHost(
                        navController = navController, startDestination = "Data",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable("Data") {
                            MatchData(
                                data = gameHistory,
                                onClickLine = { game ->
                                    tmp_gamemap = game
                                    navController.navigate("Inspect")
                                },
                                onClickFAB = {
                                    navController.navigate("Game")
                                }
                            )
                        }
                        composable("Inspect") { backStackEntry ->
                            MatchInspect(
                                game = tmp_gamemap
                            )
                        }
                        composable("Game") {
                            Game()
                        }
                    }
                }
            }
        }
    }
}