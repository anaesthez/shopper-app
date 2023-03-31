package ru.ennesterov.shopper_app.domain

class DeleteItemUseCase(private val itemListRepository: ItemListRepository) {

    fun deleteItem(item: Item) {
        itemListRepository.deleteItem(item)
    }

}