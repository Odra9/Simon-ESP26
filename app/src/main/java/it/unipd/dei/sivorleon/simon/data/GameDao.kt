package it.unipd.dei.sivorleon.simon.data

import androidx.room.Dao
import androidx.room.Query

@Dao
interface GameDao {
    @Query("SELECT * FROM game WHERE uid = :uid")
    fun get(uid: Int): Game

    @Query("SELECT * FROM game")
    fun getAll(): List<Game>
}