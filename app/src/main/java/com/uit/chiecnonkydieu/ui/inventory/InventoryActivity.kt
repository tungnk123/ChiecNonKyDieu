package com.uit.chiecnonkydieu.ui.inventory

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.uit.chiechnonkydieu.R
import com.uit.chiechnonkydieu.databinding.ActivityInventoryBinding
import com.uit.chiecnonkydieu.adapter.InventoryAdapter
import com.uit.chiecnonkydieu.adapter.StoreItemAdapter
import com.uit.chiecnonkydieu.model.GemResponse
import com.uit.chiecnonkydieu.model.StoreItem
import com.uit.chiecnonkydieu.model.toStoreItem

class InventoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityInventoryBinding
    private lateinit var storeItemDaQuyAdapter: InventoryAdapter
    private lateinit var storeItemList: List<StoreItem>

    private val inventoryViewModel: InventoryViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityInventoryBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val toolbar: Toolbar = binding.toolbar
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)


        inventoryViewModel.gemResponse.observe(this, Observer { gemResponse ->
            gemResponse?.let {
                updateUI(it)
            }
        })

        inventoryViewModel.getItems("tungnk123")
        storeItemDaQuyAdapter = InventoryAdapter(emptyList(), "tungnk123", inventoryViewModel)
        binding.rcvDaQuy.apply {
            layoutManager = LinearLayoutManager(this@InventoryActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = storeItemDaQuyAdapter
        }


    }
    private fun updateUI(gemResponse: GemResponse) {
        storeItemList = gemResponse.playerItems.map {
            it.itemDto.toStoreItem()
        }
        Log.d("Inventory api", gemResponse.toString())
        storeItemDaQuyAdapter.updateItems(storeItemList)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}