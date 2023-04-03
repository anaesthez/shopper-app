package ru.ennesterov.shopper_app.presentation

import androidx.recyclerview.widget.DiffUtil
import ru.ennesterov.shopper_app.domain.Item

class ItemDiffCallback: DiffUtil.ItemCallback<Item>() {

    override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean =
        oldItem == newItem

}