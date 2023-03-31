package ru.ennesterov.shopper_app.presentation

import androidx.lifecycle.ViewModel
import ru.ennesterov.shopper_app.data.ItemListRepositoryImpl
import ru.ennesterov.shopper_app.domain.DeleteItemUseCase
import ru.ennesterov.shopper_app.domain.EditItemUseCase
import ru.ennesterov.shopper_app.domain.GetItemListUseCase
import ru.ennesterov.shopper_app.domain.Item

class MainViewModel : ViewModel() {

    private val repository = ItemListRepositoryImpl

    private val getItemListUseCase  = GetItemListUseCase(repository)
    private val deleteItemUseCase  = DeleteItemUseCase(repository)
    private val editItemListUseCase  = EditItemUseCase(repository)

    val itemList = getItemListUseCase.getItemsList()

    fun deleteItem(item: Item) {
        deleteItemUseCase.deleteItem(item)
    }

    fun changeState(item: Item) {
        val updatedItem = item.copy(enabled = !item.enabled)
        editItemListUseCase.editItem(updatedItem)
    }

}