package ru.ennesterov.shopper_app.domain

class EditItemUseCase(private val itemListRepository: ItemListRepository) {

    fun editItem(item: Item) {
        itemListRepository.editItem(item)
    }

}