package it.unipd.dei.sivorleon.simon.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Game::class], version = 1)
abstract class GameDatabase : RoomDatabase() {
    abstract fun gameDao(): GameDao

    companion object {
        @Volatile
        private var INSTANCE: GameDatabase? = null

        fun getDatabase(context: Context): GameDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                                context.applicationContext,
                                GameDatabase::class.java,
                                "game_database"
                            ).fallbackToDestructiveMigration(true).build()  // if the database version changes, we expect conflict issues so the entire old table gets dropped
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}