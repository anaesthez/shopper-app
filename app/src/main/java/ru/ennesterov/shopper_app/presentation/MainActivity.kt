package ru.ennesterov.shopper_app.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import ru.ennesterov.shopper_app.R

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var itemListAdapter: ItemListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupRecyclerView()
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        viewModel.itemList.observe(this) {
            itemListAdapter.submitList(it)
        }
        val addButton = findViewById<FloatingActionButton>(R.id.add_item_button)
        addButton.setOnClickListener {
            val intent = ItemActivity.newIntentAddItem(this)
            startActivity(intent)
        }

    }

    private fun setupRecyclerView() {
        val recyclerViewItemList = findViewById<RecyclerView>(R.id.items_list_recyclerview)
        with(recyclerViewItemList) {
            itemListAdapter = ItemListAdapter()
            adapter = itemListAdapter
            recycledViewPool.setMaxRecycledViews(
                R.layout.item_enabled,
                ItemListAdapter.MAX_POOLS
            )
            recycledViewPool.setMaxRecycledViews(
                R.layout.item_disabled,
                ItemListAdapter.MAX_POOLS
            )
            setupLongTimeClickListener()
            setupClickListener()
            setupSwipeListener(recyclerViewItemList)
        }
    }

    private fun setupSwipeListener(recyclerViewItemList: RecyclerView) {
        val callback = object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val item = itemListAdapter.currentList[viewHolder.adapterPosition]
                viewModel.deleteItem(item)
            }
        }
        val itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(recyclerViewItemList)
    }

    private fun setupClickListener() {
        itemListAdapter.onItemClickListener = {
            val intent = ItemActivity.newIntentEditItem(this, it.id)
            startActivity(intent)
        }
    }

    private fun setupLongTimeClickListener() {
        itemListAdapter.onItemLongClickListener = {
            viewModel.changeState(it)
        }
    }

}