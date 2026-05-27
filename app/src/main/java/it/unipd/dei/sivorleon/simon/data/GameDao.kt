package it.unipd.dei.sivorleon.simon.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface GameDao {
    @Query("SELECT * FROM game WHERE uid = :uid")
    fun get(uid: Int): Game

    @Query("SELECT * FROM game ORDER BY uid DESC")
    fun getAll(): List<Game>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(game: Game)
}