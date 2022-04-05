package ru.kotofeya.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ListItemEntityDao {

    @Insert
    fun insertListItemEntity(listItemEntity: ListItemEntity)

    @Delete
    fun deleteListItemEntity(listItemEntity: ListItemEntity)

    @Query("SELECT * FROM listitementity")
    fun getAllListItemEntities(): List<ListItemEntity>
}