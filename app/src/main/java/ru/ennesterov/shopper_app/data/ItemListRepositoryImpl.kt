package ru.ennesterov.shopper_app.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.ennesterov.shopper_app.domain.Item
import ru.ennesterov.shopper_app.domain.ItemListRepository

object ItemListRepositoryImpl: ItemListRepository {

    private val itemsList = mutableListOf<Item>()
    private val itemsListLiveData = MutableLiveData<List<Item>>()

    private var autoIncrementId = 0

    override fun getItemsList(): LiveData<List<Item>> {
        return itemsListLiveData
    }

    override fun addItem(item: Item) {
        if (item.id == Item.UNDEFINED_ID) {
            item.id = autoIncrementId++
        }
        itemsList.add(item)
        updateList()
    }

    override fun deleteItem(item: Item) {
        itemsList.remove(item)
        updateList()
    }

    override fun editItem(item: Item) {
        val oldItem = getItem(item.id)
        itemsList.remove(oldItem)
        addItem(item)
    }

    override fun getItem(id: Int): Item {
        return itemsList.find { it.id == id } as Item
    }

    private fun updateList() {
        itemsListLiveData.value = itemsList.toList()
    }
}