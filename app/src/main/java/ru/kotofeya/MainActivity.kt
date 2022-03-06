package ru.kotofeya

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import ru.kotofeya.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val SP_STRINGS = "dataList"
    private lateinit var binding: ActivityMainBinding
    private val dataModel: DataModel by viewModels()
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPreferences = getSharedPreferences("LIST_SP", Context.MODE_PRIVATE)
        loadList()
        dataModel.dataSet.observe(this, {
            saveList(it)
        })
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame_layout, ListFragment.newInstance())
            .commit()
    }

    private fun loadList() {
        val set = sharedPreferences.getStringSet(SP_STRINGS, HashSet<String>())
        dataModel.dataSet.postValue(set)
    }

    private fun saveList(set: Set<String>){
        sharedPreferences.edit().putStringSet(SP_STRINGS, set).apply()
    }
}