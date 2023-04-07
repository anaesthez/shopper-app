package ru.ennesterov.shopper_app.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.ennesterov.shopper_app.R
import ru.ennesterov.shopper_app.domain.Item

class ItemActivity : AppCompatActivity(), ItemFragment.OnEditingFinishedListener {

    private var screenMode = MODE_UNKNOWN
    private var itemId = Item.UNDEFINED_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item)
        parseIntent()
        if (savedInstanceState == null) {
            setupRightMode()
        }
    }

    private fun setupRightMode() {
        val fragment = when (screenMode) {
            MODE_EDIT -> ItemFragment.newInstanceEditItem(itemId)
            MODE_ADD -> ItemFragment.newInstanceAddItem()
            else -> throw Exception("Unknown screen mode")
        }
        supportFragmentManager.beginTransaction()
            .replace(R.id.item_container, fragment)
            .commit()
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

    override fun onEditingFinished() {
        finish()
    }
}