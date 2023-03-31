package ru.ennesterov.shopper_app.domain

class AddItemUseCase(private val itemListRepository: ItemListRepository) {

    fun addItem(item: Item) {
        itemListRepository.addItem(item)
    }

}