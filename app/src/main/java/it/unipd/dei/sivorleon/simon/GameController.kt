package it.unipd.dei.sivorleon.simon

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

/**
    Game Logic will be handled by this controller
 */
class GameController () {
    // VARIABLES
    // the current sequence
    private var sequence : MutableList<Int> = mutableListOf()
    /*
        How long does the animation lasts
        This is technically half the time, as the animation is always played twice. Once the change color, and then it's played in reverse to return to previous state
     */
    val animationDuration : Long = 300
    // which tile is being currently animated in the sequence
    private var animationPointer : Int = 0
    private var userErrorDetected : Boolean = false
    private var hasGameEnded : Boolean = false
    private var hasGameBeenSaved : Boolean = false

    // All values that, when changed, need to trigger recomposition in the Game Composable need to be wrapped with MutableState
    // Points to the current position in the sequence
    private var pointer by mutableIntStateOf(0)
    var isGamePaused by mutableStateOf(false)
    var isGameActive by mutableStateOf(false)
    var isSequenceBeingAnimated by mutableStateOf(false)

    // Because the controller is created once for every app instance, it needs to be reset between games
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

    // ANIMATION
    /**
     * Used by Game composable to know if a tile has to be animated
     *
     * @param[index] Tile index
     */
    fun colorStartAnimation(index: Int) : Boolean {
        return tiles[index].animate.value
    }

    /**
     * Start animation in tile index
     * AudioController gets called to play the correct sound
     *
     * @param[index] Tile index
     */
    private fun animateColor(index: Int) {
        AudioController.getController().playTile(index)
        tiles[index].animate.value = true
    }

    /**
     * When the animation has ended, it needs to return to default state
     * This triggers the reverse animation in the Game composable
     *
     * @param[index] Tile index
     */
    fun colorAnimationHasEnded(index: Int) {
        tiles[index].animate.value = false
    }

    /**
     * Animate the whole sequence
     * suspend fun as to be non blocking
     * If the game ends during the animation, the animation ends
     */
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

        // Check if animation has ended
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

    /**
     * Randomly choose a new tile to add to end of sequence
     * Then calls animateSequence()
     */
    private fun newRandom() {
        val rand = Random.nextInt(tiles.size)
        sequence.add(rand)

        // Dispatchers.Main is the UI thread
        CoroutineScope(Dispatchers.Main).launch { animateSequence() }
    }

    /**
     * Handles all logic that follows any tile click
     *
     * @param[index] Tile index
     */
    fun tileClickHandler(index: Int) {
        animateColor(index)

        if (sequence[pointer] == index) {
            if (pointer + 1 >= sequence.size) {
                CoroutineScope(Dispatchers.Main).launch {
                    delay(2*animationDuration) // wait animations to end
                    pointer = 0
                    newRandom()
                }
            } else {
                pointer += 1
            }
        } else {
            userErrorDetected = true    // game has ended due to user error, not user choice
            endGame()
        }
    }

    fun pauseGame() {
        if (isGamePaused) { // resume animation
            isGamePaused = false
            CoroutineScope(Dispatchers.Main).launch { animateSequence() } // animateSequence starts at animationPointer
        } else {
            isGamePaused = true
        }
    }

    fun endGame() {
        hasGameEnded = true
        isGameActive = false
        isSequenceBeingAnimated = false

        // !hasGameBeenSaved check prevents accidental duplicate save
        if (!hasGameBeenSaved && sequence.size > 1) {
            CoroutineScope(Dispatchers.Main).launch { saveGame() }
        }
    }

    /**
     * Calls MainActivity.saveGame() to save the game in the database
     */
    suspend fun saveGame() {
        var finalSequence = ""
        for (i in sequence) {
            finalSequence += tiles[i].code
        }

        MainActivity.saveGame(Game(
            uid = 0,    // A value of 0 for uid tells Room to autogenerate a new unique uids
            sequence = finalSequence,
            errorPos = pointer
        ))

        hasGameBeenSaved = true
    }

    /**
     * Function used by Game composable TextField
     *
     * @return Text
     */
    fun displayTextHandler() : String {
        if (isGameActive) {
            var ret = ""
            repeat(pointer) {
                ret += tiles[sequence[it]].code
            }

            return ret
        } else {
            /*
                If the game has ended we need to return a message
                ERROR and END are code strings that Game composable references in order to use the correct string resource
             */
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

    // Singleton
    companion object {
        @Volatile
        private var INSTANCE: GameController? = null

        fun getController(): GameController {
            // synchronized prevent multiple threads to create a different Controller instance
            return INSTANCE ?: synchronized(this) {
                val instance = GameController()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}

/**
 * Each tile used in game has a Tile object that holds useful values
 *
 * @param[code] Single letter color code
 * @param[color] Color
 * @param[tone] Frequency to use for the audio sine wave
 * @param[animate] Boolean value used for click animation
 */
class Tile (var code : Char, var color : Color, var tone : Double, var animate : MutableState<Boolean> = mutableStateOf(false))
val tiles = listOf(
    Tile('R', Red, 261.63),
    Tile('G', Green, 293.66),
    Tile('B', Blue, 329.63),
    Tile('M', Magenta, 349.23),
    Tile('Y', Yellow, 392.0),
    Tile('C', Cyan, 440.0)
)