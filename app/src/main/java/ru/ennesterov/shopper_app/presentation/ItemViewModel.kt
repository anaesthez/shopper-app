package ru.ennesterov.shopper_app.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.ennesterov.shopper_app.data.ItemListRepositoryImpl
import ru.ennesterov.shopper_app.domain.AddItemUseCase
import ru.ennesterov.shopper_app.domain.EditItemUseCase
import ru.ennesterov.shopper_app.domain.GetItemUseCase
import ru.ennesterov.shopper_app.domain.Item

class ItemViewModel: ViewModel() {

    private val repository = ItemListRepositoryImpl

    private val getItemListUseCase  = GetItemUseCase(repository)
    private val addItemUseCase = AddItemUseCase(repository)
    private val editItemListUseCase  = EditItemUseCase(repository)

    private val _errorInputName = MutableLiveData<Boolean>()
    val errorInputName: LiveData<Boolean>
        get() = _errorInputName

    private val _errorInputQuantity = MutableLiveData<Boolean>()
    val errorInputQuantity: LiveData<Boolean>
        get() = _errorInputQuantity

    private val _item = MutableLiveData<Item>()
    val item: LiveData<Item>
        get() = _item

    private val _isFinished = MutableLiveData<Unit>()
    val isFinished: LiveData<Unit>
        get() = _isFinished

    fun getItem(itemId: Int) {
        val item = getItemListUseCase.getItem(itemId)
        _item.value = item
    }

    fun addItem(inputName: String?, inputQuantity: String?) {
        val name = parseInputName(inputName)
        val quantity = parseInputQuantity(inputQuantity)
        if (validateInputs(name, quantity)) {
            val item = Item(name = name, count = quantity, enabled = true)
            addItemUseCase.addItem(item)
            finishActivity()
        }
    }

    fun editItem(inputName: String?, inputQuantity: String?) {
        val name = parseInputName(inputName)
        val quantity = parseInputQuantity(inputQuantity)
        if (validateInputs(name, quantity)) {
            _item.value?.let {
                val item = it.copy(name = name, count = quantity)
                editItemListUseCase.editItem(item)
                finishActivity()
            }
        }
    }

    private fun parseInputName(name: String?): String =
        name?.trim() ?: ""

    private fun parseInputQuantity(quantity: String?): Int =
        try {
            quantity?.trim()?.toInt() ?: 0
        } catch (e: Exception) {
            0
        }

    private fun validateInputs(name: String, quantity: Int): Boolean {
        var result = true
        if (name.isBlank()) {
            _errorInputName.value = true
            result = false
        }
        if (quantity <= 0) {
            _errorInputQuantity.value = true
            result = false
        }
        return result
    }

    fun resetInputName() {
        _errorInputName.value = false
    }

    fun resetInputQuantity() {
        _errorInputQuantity.value = false
    }

    private fun finishActivity() {
        _isFinished.value = Unit
    }
}