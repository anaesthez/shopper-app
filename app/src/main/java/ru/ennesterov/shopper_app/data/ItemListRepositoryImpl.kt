package ru.ennesterov.shopper_app.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.ennesterov.shopper_app.domain.Item
import ru.ennesterov.shopper_app.domain.ItemListRepository
import kotlin.random.Random

object ItemListRepositoryImpl: ItemListRepository {

    private val itemsList = sortedSetOf<Item>({ o1, o2 ->  o1.id.compareTo(o2.id) })
    private val itemsListLiveData = MutableLiveData<List<Item>>()

    private var autoIncrementId = 0

    init {
            val item = Item("hello", 0, Random.nextBoolean())
            addItem(item)
    }

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