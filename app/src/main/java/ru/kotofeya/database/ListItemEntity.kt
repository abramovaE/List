package ru.kotofeya.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ListItemEntity(@PrimaryKey(autoGenerate = true) val id: Int? = null,
                                                      val listId: Int,
                                                      val value: String){



}