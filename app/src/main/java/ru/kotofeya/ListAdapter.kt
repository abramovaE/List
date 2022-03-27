package ru.kotofeya

import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.kotofeya.databinding.ListItemBinding

class ListAdapter(private val listChangeListener: ListChangeListener) :
    RecyclerView.Adapter<ListAdapter.ListViewHolder>(){

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

        holder.binding.textView.setOnTouchListener(View.OnTouchListener {
                view, motionEvent ->
            val rawX = motionEvent.rawX
            if(motionEvent.action == MotionEvent.ACTION_DOWN){
                mStartingX = rawX.toInt()
                view.isSelected = true
            }

            if(motionEvent.action == MotionEvent.ACTION_MOVE){
                if(mStartingX - rawX > MOVE_SIZE) {
                    Log.d(TAG, "move to left $mStartingX ${view.left} ${view.right}")
                    val string = list[position]
                    list.removeAt(position)
                    listChangeListener.onTextDelete(string)
                    view.isSelected = false
                    notifyDataSetChanged()
                }
                else if(mStartingX - rawX < 0){
                    Log.d(TAG, "move to right $mStartingX ${view.left} ${view.right}")
                }
            }
            if(motionEvent.action == MotionEvent.ACTION_UP){
                mStartingX = -1000
                view.isSelected = false
            }
            return@OnTouchListener true
        })
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