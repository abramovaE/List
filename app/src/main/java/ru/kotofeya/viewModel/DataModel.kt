package ru.kotofeya.viewModel

import android.content.Context
import androidx.lifecycle.*
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.kotofeya.database.ListEntity
import ru.kotofeya.database.ListItemEntity
import ru.kotofeya.database.Repository

open class DataModel(private val repo: Repository): ViewModel() {


    private val handler = CoroutineExceptionHandler { _, exception ->
        exception.printStackTrace()
//        println("CoroutineExceptionHandler got ${exception.stackTrace}")
//        _exText.value = "the loading was failed"
    }

    private val listItemEntities = MutableLiveData<List<ListItemEntity>>()

    fun deleteListItemEntityByName(name: String){
        viewModelScope.launch(handler) {
            withContext(Dispatchers.IO) {
                repo.deleteListItemEntityByName(name)
            }
        }
    }

    fun getListItemEntities(): LiveData<List<ListItemEntity>>{
        return listItemEntities
    }

    fun load(){
        var listItemEntitiesList: List<ListItemEntity>
        viewModelScope.launch(handler){
            withContext(Dispatchers.IO){
                listItemEntitiesList = repo.getAllListItemEntities()
            }
            listItemEntities.postValue(listItemEntitiesList)
        }
    }

    fun addNewList(listEntity: ListEntity){
        viewModelScope.launch(handler){
            withContext(Dispatchers.IO){
                repo.addNewList(listEntity)
            }
        }
    }

    fun insertNewListItemEntity(listItemEntity: ListItemEntity){
        viewModelScope.launch(handler){
            withContext(Dispatchers.IO){
                repo.insertListItemEntity(listItemEntity)
            }
        }

        if(listItemEntities.value == null){
            listItemEntities.value = emptyList()
        }
        val list = listItemEntities.value?.toMutableList()
        list?.add(listItemEntity)
        listItemEntities.postValue(list!!)
    }


    @Suppress("UNCHECKED_CAST")
    class Factory(context: Context) : ViewModelProvider.NewInstanceFactory() {
        private val repo = Repository(context.applicationContext)

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            when (modelClass) {
                DataModel::class.java -> DataModel(repo) as T
                else -> throw IllegalArgumentException()
            }
            return DataModel(repo) as T
        }
    }
}