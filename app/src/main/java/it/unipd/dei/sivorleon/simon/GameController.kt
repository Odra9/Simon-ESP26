package it.unipd.dei.sivorleon.simon

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import it.unipd.dei.sivorleon.simon.data.Game
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
    private var sequence : MutableList<Int> = mutableListOf()
    val animationDuration : Long = 300
    private var animationPointer : Int = 0
    private var userErrorDetected : Boolean = false
    private var hasGameEnded : Boolean = false
    private var hasGameBeenSaved : Boolean = false
    //All values that, when changed, need to trigger recomposition need to be wrapped with MutableState
    //Points to the current position in the sequence
    private var pointer by mutableIntStateOf(0)
    var isGamePaused by mutableStateOf(false)
    var isGameActive by mutableStateOf(false)
    var isSequenceBeingAnimated by mutableStateOf(false)

    fun resetController() {
        sequence = mutableListOf()
        animationPointer = 0
        userErrorDetected = false
        hasGameEnded = false
        hasGameBeenSaved = false
        pointer = 0
        isGamePaused = false
        isGameActive = false
        isSequenceBeingAnimated = false
    }

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

    private suspend fun animateSequence() {
        isSequenceBeingAnimated = true

        while (
            animationPointer < sequence.size
            && !isGamePaused && isGameActive
        )   {
            animateColor(sequence[animationPointer])
            delay(2*animationDuration) //using twice the duration prevents overlap
            animationPointer++
        }

        //Check if animation has ended
        if (animationPointer == sequence.size) {
            isSequenceBeingAnimated = false
            animationPointer = 0
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
            userErrorDetected = false
            endGame()
        }
    }

    //Pause or restart sequence animation
    fun pauseGame() {
        if (isGamePaused) { //resume animation
            isGamePaused = false
            CoroutineScope(Dispatchers.Main).launch { animateSequence() }
        } else {
            isGamePaused = true
        }
    }

    fun endGame() {
        hasGameEnded = true
        isGameActive = false

        if (!hasGameBeenSaved && sequence.size > 1) {
            CoroutineScope(Dispatchers.Main).launch { saveGame() }
        }
    }

    //TODO
    suspend fun saveGame() {
        Log.v(null, "SAVING GAME")

        var finalSequence = ""
        for (i in sequence) {
            finalSequence += tiles[i].code
        }

        MainActivity.saveGame(Game(
            uid = 0,    //autogenerate
            sequence = finalSequence,
            errorPos = pointer
        ))

        hasGameBeenSaved = true
    }

    fun displayTextHandler() : String {
        if (isGameActive) {
            var ret = ""
            repeat(pointer) {
                ret += tiles[sequence[it]].code + ", "
            }

            return ret.slice(IntRange(0, ret.length - 2))
        } else {
            return if (hasGameEnded) {
                if (userErrorDetected) {
                    "ERROR"
                } else {
                    "END"
                }
            } else {
                ""
            }
        }
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