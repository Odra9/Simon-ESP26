package it.unipd.dei.sivorleon.simon

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.setValue

//Game Logic will be handled by this class, instantiated in the game composable
class GameController (
    var current : String = "", var max : String = "", var errorPos : Int = -1,
    var isGameActive : Boolean = false, isGamePaused : Boolean = false
) {
    //All values that, when changed, need to trigger recomposition need to be wrapped with MutableState
    var isGamePaused by mutableStateOf(isGamePaused)
    fun startGame() {
        isGameActive = true
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

    //the saver companion object is needed in order to instantiate this class with the remember API
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