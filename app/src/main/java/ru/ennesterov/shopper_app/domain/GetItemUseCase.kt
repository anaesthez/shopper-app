package ru.ennesterov.shopper_app.domain

class GetItemUseCase(private val itemListRepository: ItemListRepository) {

    fun getItem(id: Int): Item {
        return itemListRepository.getItem(id)
    }

}