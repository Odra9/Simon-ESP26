package it.unipd.dei.sivorleon.simon.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Game (
    @PrimaryKey(autoGenerate = true) val uid: Int,
    @ColumnInfo val sequence: String,
    @ColumnInfo val errorPos: Int
)