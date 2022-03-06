package ru.kotofeya

import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import ru.kotofeya.databinding.ListFragmentBinding

class ListFragment: Fragment(), ListChangeListener {
    private lateinit var binding: ListFragmentBinding
    private val dataModel: DataModel by activityViewModels()
    private val listAdapter = ListAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ListFragmentBinding.inflate(inflater)
        dataModel.dataSet.observe(activity as LifecycleOwner, {
            listAdapter.updateList(it)
        })


        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        binding.apply {
            list.layoutManager = LinearLayoutManager(this@ListFragment.context)
            list.adapter = listAdapter
            inputItem.setOnKeyListener(View.OnKeyListener{
                    _, keyCode, event ->
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                    val newString = inputItem.text.toString()
                    if(newString.isNotBlank()) {
                        val dataList = HashSet<String>()
                        dataModel.dataSet.value?.let { dataList.addAll(it) }
                        dataList.add(newString)
                        dataModel.dataSet.postValue(dataList)
                        inputItem.text.clear()
//                        val inputManager = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//                        inputManager.hideSoftInputFromWindow(view.windowToken, 0)
                    }
                    return@OnKeyListener true
                }
                return@OnKeyListener false
            })
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = ListFragment()
    }

    override fun onTextDelete(text: String) {
        val dataList = HashSet<String>()
        dataModel.dataSet.value?.let { dataList.addAll(it) }
        dataList.remove(text)
        dataModel.dataSet.postValue(dataList)
    }
}