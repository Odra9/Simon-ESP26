package it.unipd.dei.sivorleon.simon

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import it.unipd.dei.sivorleon.simon.data.*
import it.unipd.dei.sivorleon.simon.ui.*
import it.unipd.dei.sivorleon.simon.ui.theme.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    companion object {
        var db : GameDatabase? = null
        var gameHistory : MutableList<Game>? = null

        private fun populateHistory() {
            gameHistory = db!!.gameDao().getAll().toMutableList()
        }

        suspend fun saveGame(game: Game) {
            gameHistory!!.add(0, game) //add as first in list (List is ordered DESC by Game.uid)

            db!!.gameDao().insert(game)
        }
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        db = GameDatabase.getDatabase(applicationContext)
        lifecycleScope.launch(Dispatchers.IO){populateHistory()}

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
                                onClickLine = { gameIndex ->
                                    navController.navigate("Inspect/${gameIndex}")
                                },
                                onClickFAB = {
                                    controller.resetController()
                                    navController.navigate("Game")
                                }
                            )
                        }
                        composable("Inspect/{index}") { backStackEntry ->
                            MatchInspect(
                                game = gameHistory!![Uri.decode(backStackEntry.arguments?.getString("index")).toInt()]
                            )
                        }
                        composable("Game") {
                            Game(
                                onEndGame = { navController.popBackStack() }
                            )
                        }
                    }
                }
            }
        }
    }
}