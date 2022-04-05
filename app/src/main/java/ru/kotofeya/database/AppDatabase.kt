package ru.kotofeya.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ListEntity::class, ListItemEntity::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun listEntityDao(): ListEntityDao
    abstract fun listItemEntityDao(): ListItemEntityDao

    companion object {
        var instance: AppDatabase? = null
        val databaseName = "list_db"

        fun getAppDataBase(context: Context): AppDatabase?{
            if(instance == null){
                synchronized(AppDatabase::class){
                    instance = Room.databaseBuilder(context.applicationContext,
                        AppDatabase::class.java, databaseName).build()
                }
            }
            return instance
        }

        fun destroyDataBase(){
            instance = null;
        }
    }
}