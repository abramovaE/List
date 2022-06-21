package ru.kotofeya.database

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class Repository(appContext: Context) {

    private val db = AppDatabase.getAppDataBase(appContext)

    suspend fun getAllListItemEntities(): List<ListItemEntity> = withContext(Dispatchers.IO) {
        db?.listItemEntityDao()?.getAllListItemEntities()!!
    }

    suspend fun deleteListItemEntityByName(entityName: String) = withContext(Dispatchers.IO){
        db?.listItemEntityDao()?.deleteListItemEntityByName(entityName)
    }

    suspend fun insertListItemEntity(listItemEntity: ListItemEntity) = withContext(Dispatchers.IO){
        db?.listItemEntityDao()?.insertListItemEntity(listItemEntity)
    }


    suspend fun addNewList(listEntity: ListEntity) = withContext(Dispatchers.IO){
        db?.listEntityDao()?.insertListEntity(listEntity)
    }

    suspend fun updateListItemEntity(listItemEntity: ListItemEntity) = withContext(Dispatchers.IO){
        db?.listItemEntityDao()?.updateListItemEntity(listItemEntity)
    }

    suspend fun getListItemEntityById(id: Int) = withContext(Dispatchers.IO){
        db?.listItemEntityDao()?.getListItemEntityById(id)
    }

    suspend fun removeAllListItemEntities() = withContext(Dispatchers.IO){
        db?.listItemEntityDao()?.removeAllListItemEntities();
    }
}