package ru.ennesterov.shopper_app.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import ru.ennesterov.shopper_app.R
import ru.ennesterov.shopper_app.domain.Item

class ItemListAdapter: ListAdapter<Item, ItemViewHolder>(ItemDiffCallback()) {

    var onItemLongClickListener: ((Item) -> Unit)? = null
    var onItemClickListener: ((Item) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            viewType,
            parent,
            false
        )
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = getItem(position)
        holder.itemHeader.text = item.id.toString()
        holder.itemQuantity.text = item.name
        holder.view.setOnLongClickListener {
            onItemLongClickListener?.invoke(item)
            true
        }
        holder.view.setOnClickListener {
            onItemClickListener?.invoke(item)
        }
    }

    override fun getItemViewType(position: Int): Int =
        if (getItem(position).enabled)
            R.layout.item_enabled
        else
            R.layout.item_disabled



    companion object {

        const val MAX_POOLS = 15

    }
}