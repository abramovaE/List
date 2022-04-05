package ru.kotofeya.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ListEntity(@PrimaryKey(autoGenerate = true) val id: Int? = null,
                      val name: String)
