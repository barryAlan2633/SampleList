package com.barryalan.samplelist

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.barryalan.samplelist.model.ListItem

class ListAdapter(
    private val itemList: MutableList<ListItem>
) : RecyclerView.Adapter<ListAdapter.ListViewHolder>() {

    private var filteredItemList = itemList
    private var itemBackgroundCounter = 0

    class ListViewHolder(var view: View) : RecyclerView.ViewHolder(view)


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.list_item, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        itemBackgroundCounter++
        if (itemBackgroundCounter % 5 == 0) {
            holder.view.findViewById<LinearLayout>(R.id.lin_lyt_list_item)
                .setBackgroundColor(Color.argb(100, 115, 115, 115))
        }else{
            holder.view.findViewById<LinearLayout>(R.id.lin_lyt_list_item)
                .setBackgroundColor(Color.TRANSPARENT)
        }
        holder.view.findViewById<TextView>(R.id.tv_list_id).text =
            holder.view.context.getString(R.string.ListIdText, filteredItemList[position].listId)
        holder.view.findViewById<TextView>(R.id.tv_id).text =
            holder.view.context.getString(R.string.IdText, filteredItemList[position].id)
        holder.view.findViewById<TextView>(R.id.tv_name).text =
            holder.view.context.getString(R.string.ListNameText, filteredItemList[position].name)
    }

    override fun getItemCount() = filteredItemList.size

    fun updateList(updatedList: MutableList<ListItem>) {
        itemList.clear()
        itemList.addAll(updatedList)
        filter()
        notifyDataSetChanged()
    }

    private fun filter() {

        val newList = mutableListOf<ListItem>()
        itemList.map {
            if (it.name != null && it.name.isNotEmpty()) {
                newList.add(it)
            }
        }

        filteredItemList = newList.sortedWith(
            compareBy(
                { it.listId.toString().length },
                { it.listId },
                { it.name.toString().length },
                { it.name })
        ) as MutableList<ListItem>

    }

}