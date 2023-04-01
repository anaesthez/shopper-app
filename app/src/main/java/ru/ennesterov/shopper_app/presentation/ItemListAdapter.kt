package ru.ennesterov.shopper_app.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.ennesterov.shopper_app.R
import ru.ennesterov.shopper_app.domain.Item

class ItemListAdapter: RecyclerView.Adapter<ItemListAdapter.ItemViewHolder>() {

    var itemsList = listOf<Item>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var onItemLongClickListener: ((Item) -> Unit)? = null
    lateinit var onItemClickListener: (Item) -> Unit

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            viewType,
            parent,
            false
        )
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = itemsList[position]
        holder.itemHeader.text = item.id.toString()
        holder.itemQuantity.text = item.name
        holder.view.setOnLongClickListener {
            onItemLongClickListener?.invoke(item)
            true
        }
        holder.view.setOnClickListener {
            onItemClickListener(item)
        }

    }

    override fun onViewRecycled(holder: ItemViewHolder) {
        super.onViewRecycled(holder)
        holder.itemHeader.text = ""
        holder.itemQuantity.text = ""
    }

    override fun getItemViewType(position: Int): Int {
        return if(itemsList[position].enabled) {
            R.layout.item_enabled
        } else {
            R.layout.item_disabled
        }
    }

    override fun getItemCount(): Int = itemsList.size

    class ItemViewHolder(val view: View): RecyclerView.ViewHolder(view) {
        val itemHeader: TextView = view.findViewById(R.id.item_header_text_view)
        val itemQuantity: TextView = view.findViewById(R.id.item_quantity_text_view)
    }

    companion object {

        const val MAX_POOLS = 15

    }
}