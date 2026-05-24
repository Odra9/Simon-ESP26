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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

//Game Logic will be handled by this class, instantiated in the game composable
class GameController () {
    //VARIABLES
    var sequence : MutableList<Int> = mutableListOf()
    val animationDuration : Long = 300
    //All values that, when changed, need to trigger recomposition need to be wrapped with MutableState
    //Points to the current position in the sequence
    var pointer by mutableIntStateOf(0)
    var isGamePaused by mutableStateOf(false)
    var isGameActive by mutableStateOf(false)

    //ANIMATION
    fun colorStartAnimation(index: Int) : Boolean {
        return tiles[index].animate.value
    }

    private fun animateColor(index: Int) {
        tiles[index].animate.value = true
    }

    fun colorAnimationHasEnded(index: Int) {
        tiles[index].animate.value = false
    }

    suspend fun animateSequence() {
        repeat(sequence.size) {
            animateColor(sequence[it])
            delay(2*animationDuration) //using twice the duration prevents overlap
        }
    }

    //GAME LOGIC
    fun startGame() {
        isGameActive = true

        newRandom()
    }

    private fun newRandom() {
        val rand = Random.nextInt(tiles.size)
        sequence.add(rand)

        CoroutineScope(Dispatchers.Main).launch { animateSequence() }
    }

    fun tileClickHandler(index: Int) {
        animateColor(index)

        if (sequence[pointer] == index) {
            if (pointer + 1 >= sequence.size) {
                CoroutineScope(Dispatchers.Main).launch {
                    delay(2*animationDuration)
                    pointer = 0
                    newRandom()
                }
            } else {
                pointer += 1
            }
        } else {
            endGame()
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