package ru.kotofeya.database

import androidx.room.*

@Dao
interface ListItemEntityDao {

    @Insert
    fun insertListItemEntity(listItemEntity: ListItemEntity)

    @Delete
    fun deleteListItemEntity(listItemEntity: ListItemEntity)

    @Query("SELECT * FROM listitementity ORDER BY id")
    fun getAllListItemEntities(): List<ListItemEntity>

    @Query("DELETE FROM listitementity WHERE value=:name")
    fun deleteListItemEntityByName(name: String)

    @Update
    fun updateListItemEntity(listItemEntity: ListItemEntity)

    @Query("SELECT * FROM listitementity WHERE id=:id")
    fun getListItemEntityById(id: Int): ListItemEntity

    @Query("DELETE FROM listitementity")
    fun removeAllListItemEntities()


}