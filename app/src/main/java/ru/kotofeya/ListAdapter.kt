package ru.kotofeya

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.kotofeya.databinding.ListItemBinding

class ListAdapter(private val listChangeListener: ListChangeListener) : RecyclerView.Adapter<ListAdapter.ListViewHolder>(){

    private val list = ArrayList<String>()

    class ListViewHolder(item: View): RecyclerView.ViewHolder(item) {
        val binding = ListItemBinding.bind(item)
        fun bind(text: String) = with(binding){
            checkBox.text = text
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(list[position])
        holder.binding.checkBox.setOnCheckedChangeListener {
                _, b ->
            if(b) {
                val string = list[position]
                list.removeAt(position)
                listChangeListener.onTextDelete(string)
                holder.binding.checkBox.isChecked = !b
                notifyDataSetChanged()
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun updateList(set: Set<String>){
        list.clear()
        list.addAll(set)
        notifyDataSetChanged()
    }
}