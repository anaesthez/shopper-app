package ru.ennesterov.shopper_app.presentation

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import ru.ennesterov.shopper_app.R

class MainActivity : AppCompatActivity(), ItemFragment.OnEditingFinishedListener {

    private lateinit var viewModel: MainViewModel
    private lateinit var itemListAdapter: ItemListAdapter
    private var itemContainer: FragmentContainerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        itemContainer = findViewById(R.id.item_container)
        setupRecyclerView()
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        viewModel.itemList.observe(this) {
            itemListAdapter.submitList(it)
        }
        val addButton = findViewById<FloatingActionButton>(R.id.add_item_button)
        addButton.setOnClickListener {
            if (itemContainer == null) {
                val intent = ItemActivity.newIntentAddItem(this)
                startActivity(intent)
            } else {
                launchFragment(ItemFragment.newInstanceAddItem())
            }
        }

    }

    private fun launchFragment(fragment: Fragment) {
        supportFragmentManager.popBackStack()
        supportFragmentManager.beginTransaction()
            .replace(R.id.item_container, fragment)
            .addToBackStack(null)
            .commit()
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
            if (itemContainer == null) {
                val intent = ItemActivity.newIntentEditItem(this, it.id)
                startActivity(intent)
            } else {
                launchFragment(ItemFragment.newInstanceEditItem(it.id))
            }
        }
    }

    private fun setupLongTimeClickListener() {
        itemListAdapter.onItemLongClickListener = {
            viewModel.changeState(it)
        }
    }

    override fun onEditingFinished() {
        Toast.makeText(this@MainActivity, "Success", Toast.LENGTH_SHORT).show()
        supportFragmentManager.popBackStack()
    }
}