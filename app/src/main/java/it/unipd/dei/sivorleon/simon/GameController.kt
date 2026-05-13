package it.unipd.dei.sivorleon.simon

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import it.unipd.dei.sivorleon.simon.ui.theme.Blue
import it.unipd.dei.sivorleon.simon.ui.theme.Cyan
import it.unipd.dei.sivorleon.simon.ui.theme.Green
import it.unipd.dei.sivorleon.simon.ui.theme.Magenta
import it.unipd.dei.sivorleon.simon.ui.theme.Red
import it.unipd.dei.sivorleon.simon.ui.theme.Yellow
import kotlin.random.Random

//Game Logic will be handled by this class, instantiated in the game composable
class GameController (
    current : String = "", var sequence : MutableList<Int> = mutableListOf(), var errorPos : Int = -1,
    var isGameActive : Boolean = false, isGamePaused : Boolean = false
) {
    //VARIABLES: All values that, when changed, need to trigger recomposition need to be wrapped with MutableState
    var current by mutableStateOf(current)
    var isGamePaused by mutableStateOf(isGamePaused)

    //ANIMATION
//  

    fun colorStartAnimation(index: Int) : Boolean {
        return tiles[index].animate.value
    }

    fun colorAnimationHasEnded(index: Int) {
        tiles[index].animate.value = false
    }

    private fun animateColor(index: Int) {
        tiles[index].animate.value = true
    }

    //GAME LOGIC
    fun startGame() {
        isGameActive = true

        newRandom()
        animateSequence()
    }

    private fun newRandom() {
        val rand = Random.nextInt(tiles.size)
        sequence.add(rand)
        animateColor(rand)
    }

    private fun animateSequence() {
        for (i in sequence) {
            animateColor(i)
            //DELAY
        }
    }

    //TO DO
    fun pauseGame() {
        if (isGamePaused) {
            isGamePaused = false
        } else {
            isGamePaused = true
        }
    }

    fun endGame() {
        isGameActive = false
    }

    //SAVER: the saver companion object is needed in order to instantiate this class with the remember API
    companion object {
        val Saver: Saver<GameController, Any> = listSaver(
            save = { listOf(it.current, it.max, it.errorPos, it.isGameActive, it.isGamePaused) },
            restore = {
                GameController(
                    current = it[0] as String,
                    max = it[1] as String,
                    errorPos = it[2] as Int,
                    isGameActive = it[3] as Boolean,
                    isGamePaused = it[4] as Boolean
                )
            }
        )
    }
}
class Tile (var code : Char, var color : Color, var animate : MutableState<Boolean> = mutableStateOf(false))
val tiles = listOf(
    Tile('R', Red),
    Tile('G', Green),
    Tile('B', Blue),
    Tile('M', Magenta),
    Tile('Y', Yellow),
    Tile('C', Cyan)
)