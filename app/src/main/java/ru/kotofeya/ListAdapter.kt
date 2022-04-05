package ru.kotofeya

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.kotofeya.databinding.ListItemBinding
import java.util.*
import kotlin.collections.ArrayList

class ListAdapter(private val listChangeListener: ListChangeListener) :
    RecyclerView.Adapter<ListAdapter.ListViewHolder>(), ItemTouchHelperAdapter {

    private val list = ArrayList<String>()
    private val TAG = this.javaClass.simpleName

    private val MOVE_SIZE = 80
    private var mStartingX = -1000

    class ListViewHolder(item: View): RecyclerView.ViewHolder(item) {
        val binding = ListItemBinding.bind(item)
        fun bind(text: String) = with(binding){
            textView.text = text
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun updateList(set: Set<String>){
        list.clear()
        list.addAll(set)
        notifyDataSetChanged()
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int): Boolean {
        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition) {
                Collections.swap(list, i, i + 1);
            }
        } else {
            for (i in fromPosition downTo toPosition + 1) {
                Collections.swap(list, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    override fun onItemDismiss(position: Int) {
        val string = list[position]
        list.removeAt(position)
        listChangeListener.onTextDelete(string)
        notifyItemRemoved(position)
    }
}