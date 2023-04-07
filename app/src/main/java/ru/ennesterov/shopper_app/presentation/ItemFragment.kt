package ru.ennesterov.shopper_app.presentation

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputLayout
import ru.ennesterov.shopper_app.R
import ru.ennesterov.shopper_app.domain.Item

class ItemFragment: Fragment() {

    private lateinit var viewModel: ItemViewModel
    private lateinit var onEditingFinishedListener: OnEditingFinishedListener

    private lateinit var textInputLayoutName: TextInputLayout
    private lateinit var textInputLayoutQuantity: TextInputLayout
    private lateinit var editTextName: EditText
    private lateinit var editTextQuantity: EditText
    private lateinit var buttonApply: Button

    private var screenMode = MODE_UNKNOWN
    private var itemId = Item.UNDEFINED_ID

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnEditingFinishedListener)  {
            onEditingFinishedListener = context
        } else {
            throw RuntimeException("Activity must implement OnEditingFinishedListener")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseParams()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_item, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[ItemViewModel::class.java]
        initViews(view)
        setupQuantityChangedListener()
        setupTextChangedListener()
        setupRightMode()
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.errorInputQuantity.observe(viewLifecycleOwner) {
            val message = if (it) {
                getString(R.string.error_input_quantity)
            } else {
                null
            }
            textInputLayoutQuantity.error = message
        }
        viewModel.errorInputName.observe(viewLifecycleOwner) {
            val message = if (it) {
                getString(R.string.error_input_name)
            } else {
                null
            }
            textInputLayoutName.error = message
        }
        viewModel.isFinished.observe(viewLifecycleOwner) {
            onEditingFinishedListener.onEditingFinished()
        }
    }

    private fun setupRightMode() {
        when (screenMode) {
            MODE_EDIT -> launchEditMode()
            MODE_ADD -> launchAddMode()
            else -> throw Exception("Unknown screen mode")
        }
    }

    private fun launchEditMode() {
        viewModel.getItem(itemId)
        viewModel.item.observe(viewLifecycleOwner) {
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

    private fun initViews(view: View) {
        textInputLayoutName = view.findViewById(R.id.item_name_container)
        textInputLayoutQuantity = view.findViewById(R.id.quantity_container)
        editTextName = view.findViewById(R.id.item_name_edit_text)
        editTextQuantity = view.findViewById(R.id.quantity_edit_text)
        buttonApply = view.findViewById(R.id.apply_button)
    }

    private fun parseParams() {
        val args = requireArguments()
        if (!args.containsKey(SCREEN_MODE)) {
            throw RuntimeException("You need to specify screen mode param")
        }
        val mode = args.getString(SCREEN_MODE)
        if (mode != MODE_EDIT && mode != MODE_ADD) {
            throw RuntimeException("Unknown screen mode $mode")
        }
        screenMode = mode
        if (screenMode == MODE_EDIT) {
            if (!args.containsKey(ITEM_ID)) {
                throw RuntimeException("You need to specify item id param")
            }
            itemId = args.getInt(ITEM_ID, Item.UNDEFINED_ID)
        }
    }

    interface OnEditingFinishedListener {

        fun onEditingFinished()
        
    }

    companion object {
        private const val SCREEN_MODE = "SCREEN_MODE"
        private const val ITEM_ID = "ITEM_ID"
        private const val MODE_ADD = "MODE_ADD"
        private const val MODE_EDIT = "MODE_EDIT"

        private const val MODE_UNKNOWN = ""

        fun newInstanceAddItem(): ItemFragment {
            return ItemFragment().apply {
                arguments = Bundle().apply {
                    putString(SCREEN_MODE, MODE_ADD)
                }
            }
        }
        fun newInstanceEditItem(itemId: Int): ItemFragment {
            return ItemFragment().apply {
                arguments = Bundle().apply {
                    putString(SCREEN_MODE, MODE_EDIT)
                    putInt(ITEM_ID, itemId)
                }
            }
        }
    }
}