package ru.ennesterov.shopper_app.presentation

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.ennesterov.shopper_app.R

class ItemViewHolder(val view: View): RecyclerView.ViewHolder(view) {
    val itemHeader: TextView = view.findViewById(R.id.item_header_text_view)
    val itemQuantity: TextView = view.findViewById(R.id.item_quantity_text_view)
}