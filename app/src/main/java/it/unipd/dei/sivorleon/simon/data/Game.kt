package it.unipd.dei.sivorleon.simon.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Game (
    @PrimaryKey val uid: Int,
    @ColumnInfo("sequence") val sequence: String,
    @ColumnInfo("error_pos") val errorPos: Int
)