package ru.kotofeya.database

import androidx.room.*
import com.google.android.material.circularreveal.CircularRevealHelper


@Dao
interface ListEntityDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertListEntity(listEntity: ListEntity)

    @Delete
    fun delete(listEntity: ListEntity)


    @Query("SELECT * FROM listentity")
    fun getAllListEntities(): List<ListEntity>;

}