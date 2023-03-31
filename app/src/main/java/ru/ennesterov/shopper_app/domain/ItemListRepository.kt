package ru.ennesterov.shopper_app.domain

import androidx.lifecycle.LiveData

interface ItemListRepository {

    fun getItemsList(): LiveData<List<Item>>

    fun addItem(item: Item)

    fun deleteItem(item: Item)

    fun editItem(item: Item)

    fun getItem(id: Int): Item

}