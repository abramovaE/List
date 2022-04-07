package ru.kotofeya.view

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import ru.kotofeya.R
import ru.kotofeya.database.ListEntity
import ru.kotofeya.database.ListItemEntity
import ru.kotofeya.databinding.ListFragmentBinding
import ru.kotofeya.viewModel.DataModel

class ListFragment: Fragment(), ListChangeListener {
    private lateinit var binding: ListFragmentBinding
    private lateinit var dataModel: DataModel
    private val listAdapter = ListAdapter(this)

    private lateinit var itemTouchHelper: ItemTouchHelper

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ListFragmentBinding.inflate(inflater)
        val listItemTouchHelperCallback = ListItemTouchHelperCallback(listAdapter)
        itemTouchHelper = ItemTouchHelper(listItemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(binding.list)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dataModel = ViewModelProvider((activity as MainActivity), DataModel.Factory(activity as MainActivity)).get(
            DataModel::class.java)
        dataModel.getListItemEntities().observe(activity as LifecycleOwner, {
            listAdapter.updateList(it)
        })

        binding.apply {
            list.layoutManager = LinearLayoutManager(this@ListFragment.context)
            list.adapter = listAdapter
            inputItem.setOnEditorActionListener { _, keyCode, _ ->
                if (keyCode == EditorInfo.IME_ACTION_DONE) {
                    val newString = inputItem.text.toString()
                    if (newString.isNotBlank()) {
                        val listItemEntity = ListItemEntity(value = newString, listId = 0)
                        dataModel.insertNewListItemEntity(listItemEntity)
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


//            btnNewList.setOnClickListener(View.OnClickListener { v ->
//                var alertDialog = AlertDialog.Builder(context)
//                    .setPositiveButton()
//                    .create()
//                    .show()
//            })
        }
    }


    private fun showDialog(title: String) {
        val dialog = context?.let {
            Dialog(it)
        }
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.setCancelable(false)
        dialog?.setContentView(R.layout.add_new_list_dialog)
        val text = dialog?.findViewById(R.id.text) as EditText
        val btnOk = dialog?.findViewById(R.id.btn_ok) as Button
        val btnCancel = dialog?.findViewById(R.id.btn_cancel) as Button
        btnOk.setOnClickListener{
            var newList = ListEntity(name = text.text.toString())
            dataModel.addNewList(newList)
        }
        btnCancel.setOnClickListener{
            dialog.dismiss()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = ListFragment()
    }

    override fun onTextDelete(text: String) {
        dataModel.deleteListItemEntityByName(text)
    }
}