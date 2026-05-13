package it.unipd.dei.sivorleon.simon

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
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
class GameController () {
    //VARIABLES
    var sequence : MutableList<Int> = mutableListOf()
    var isGameActive : Boolean = false

    //All values that, when changed, need to trigger recomposition need to be wrapped with MutableState
    //Points to the current position in the sequence
    var pointer by mutableIntStateOf(0)
    var isGamePaused by mutableStateOf(false)

    //ANIMATION
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
        animateSequence()
    }

    private fun animateSequence() {
        for (i in sequence) {
            animateColor(i)
            //DELAY
        }
    }

    fun tileClickHandler(index: Int) {
        if (pointer+1 >= sequence.size) {
            newRandom()
            pointer = 0
        } else {
            pointer += 1
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

    fun displayText() : String {
        var ret = ""
        repeat(pointer) {
            ret += tiles[sequence[it]].code + ", "
        }

        return ret.slice(IntRange(0, ret.length - 2))
    }
}
val controller = GameController()

class Tile (var code : Char, var color : Color, var animate : MutableState<Boolean> = mutableStateOf(false))
val tiles = listOf(
    Tile('R', Red),
    Tile('G', Green),
    Tile('B', Blue),
    Tile('M', Magenta),
    Tile('Y', Yellow),
    Tile('C', Cyan)
)