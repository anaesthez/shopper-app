package ru.ennesterov.shopper_app.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputLayout
import ru.ennesterov.shopper_app.R
import ru.ennesterov.shopper_app.domain.Item

class ItemActivity : AppCompatActivity() {

    private lateinit var viewModel: ItemViewModel

    private lateinit var textInputLayoutName: TextInputLayout
    private lateinit var textInputLayoutQuantity: TextInputLayout
    private lateinit var editTextName: EditText
    private lateinit var editTextQuantity: EditText
    private lateinit var buttonApply: Button

    private var screenMode = MODE_UNKNOWN
    private var itemId = Item.UNDEFINED_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item)
        parseIntent()
        viewModel = ViewModelProvider(this)[ItemViewModel::class.java]
        initViews()
        setUpRightMode()
        setupQuantityChangedListener()
        setupTextChangedListener()
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.errorInputQuantity.observe(this) {
            val message = if (it) {
                getString(R.string.error_input_quantity)
            } else {
                null
            }
            textInputLayoutQuantity.error = message
        }
        viewModel.errorInputName.observe(this) {
            val message = if (it) {
                getString(R.string.error_input_name)
            } else {
                null
            }
            textInputLayoutName.error = message
        }
        viewModel.isFinished.observe(this) {
            finish()
        }
    }

    private fun setUpRightMode() {
        when (screenMode) {
            MODE_EDIT -> launchEditMode()
            MODE_ADD -> launchAddMode()
            else -> throw Exception("Unknown screen mode")
        }
    }

    private fun launchEditMode() {
        viewModel.getItem(itemId)
        viewModel.item.observe(this) {
            editTextName.setText(it.name)
            editTextQuantity.setText(it.count.toString())
        }
        buttonApply.setOnClickListener {
            viewModel.editItem(editTextName.text?.toString(), editTextQuantity.text?.toString())
        }
    }

    private fun launchAddMode() {
        buttonApply.setOnClickListener {
            viewModel.addItem(editTextName.text?.toString(), editTextQuantity.text?.toString())
        }
    }

    private fun setupQuantityChangedListener() {
        editTextQuantity.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.resetInputQuantity()
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })
    }

    private fun setupTextChangedListener() {
        editTextName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.resetInputName()
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })
    }

    private fun initViews() {
        textInputLayoutName = findViewById(R.id.item_name_container)
        textInputLayoutQuantity = findViewById(R.id.quantity_container)
        editTextName = findViewById(R.id.item_name_edit_text)
        editTextQuantity = findViewById(R.id.quantity_edit_text)
        buttonApply = findViewById(R.id.apply_button)
    }

    private fun parseIntent() {
        if (!intent.hasExtra(EXTRA_SCREEN_MODE)) {
            throw RuntimeException("You need to specify screen mode param")
        }
        val mode = intent.getStringExtra(EXTRA_SCREEN_MODE)
        if (mode != MODE_EDIT && mode != MODE_ADD) {
            throw RuntimeException("Unknown screen mode $mode")
        }
        screenMode = mode
        if (screenMode == MODE_EDIT) {
            if (!intent.hasExtra(EXTRA_ITEM_ID)) {
                throw RuntimeException("You need to specify item id param")
            }
            itemId = intent.getIntExtra(EXTRA_ITEM_ID, Item.UNDEFINED_ID)
        }
    }

    companion object {

        private const val EXTRA_SCREEN_MODE = "SCREEN_MODE"
        private const val EXTRA_ITEM_ID = "EXTRA_ITEM_ID"
        private const val MODE_ADD = "MODE_ADD"
        private const val MODE_EDIT = "MODE_EDIT"

        private const val MODE_UNKNOWN = ""

        fun newIntentAddItem(context: Context): Intent {
            val intent = Intent(context, ItemActivity::class.java)
            intent.putExtra(EXTRA_SCREEN_MODE, MODE_ADD)
            return intent
        }

        fun newIntentEditItem(context: Context, itemId: Int): Intent {
            val intent = Intent(context, ItemActivity::class.java)
            intent.putExtra(EXTRA_SCREEN_MODE, MODE_EDIT)
            intent.putExtra(EXTRA_ITEM_ID, itemId)
            return intent
        }
    }
}