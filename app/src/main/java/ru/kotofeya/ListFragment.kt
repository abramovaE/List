package ru.kotofeya

import android.os.Bundle
import android.view.*
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.ItemTouchHelper
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


        val listItemTouchHelperCallback = ListItemTouchHelperCallback(listAdapter)
        val itemTouchHelper = ItemTouchHelper(listItemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(binding.list)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            list.layoutManager = LinearLayoutManager(this@ListFragment.context)
            list.adapter = listAdapter
            inputItem.setOnEditorActionListener { _, keyCode, _ ->
                if (keyCode == EditorInfo.IME_ACTION_DONE) {
                    val newString = inputItem.text.toString()
                    if (newString.isNotBlank()) {
                        val dataList = HashSet<String>()
                        dataModel.dataSet.value?.let { dataList.addAll(it) }
                        dataList.add(newString)
                        dataModel.dataSet.postValue(dataList)
                        inputItem.text.clear()
                    }
                    true
                }
                false
            }

            btnRec.setOnTouchListener(View.OnTouchListener{
                    _, event ->
                if(event.action == MotionEvent.ACTION_DOWN){
                    (activity as MainActivity).startRec()
                }
                if(event.action == MotionEvent.ACTION_UP){
                    (activity as MainActivity).stopRec()
                }
                return@OnTouchListener true
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