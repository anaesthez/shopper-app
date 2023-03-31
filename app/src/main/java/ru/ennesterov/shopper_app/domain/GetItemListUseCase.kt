package ru.ennesterov.shopper_app.domain

import androidx.lifecycle.LiveData

class GetItemListUseCase(private val itemListRepository: ItemListRepository) {

    fun getItemsList(): LiveData<List<Item>> {
        return itemListRepository.getItemsList()
    }

}