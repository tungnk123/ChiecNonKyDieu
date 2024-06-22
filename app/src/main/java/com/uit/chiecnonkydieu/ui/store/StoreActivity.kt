package com.uit.chiecnonkydieu.ui.store

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.uit.chiechnonkydieu.R
import com.uit.chiechnonkydieu.databinding.ActivityStoreBinding
import com.uit.chiecnonkydieu.adapter.StoreItemAdapter
import com.uit.chiecnonkydieu.model.StoreItem

class StoreActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStoreBinding

    private lateinit var storeItemDaQuyAdapter: StoreItemAdapter
    private lateinit var storeItemNFTAdapter: StoreItemAdapter
//    private lateinit var storeItemViewModel: StoreItemViewModel
    private lateinit var storeItemList: List<StoreItem>
    private lateinit var storeItemNFTList: List<StoreItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityStoreBinding.inflate(layoutInflater)
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

        storeItemList = listOf(
            StoreItem(
                "Đá quý 1",
                R.drawable.account_circle_24px,
                isPurchased = false,
                itemPrice = 20
            ),
            StoreItem(
                "Đá quý 2",
                R.drawable.account_circle_24px,
                isPurchased = false,
                itemPrice = 20
            ),
            StoreItem(
                "Đá quý 3",
                R.drawable.account_circle_24px,
                isPurchased = false,
                itemPrice = 20
            )
        )
        storeItemNFTList = listOf(
            StoreItem(
                "NFT 1",
                R.drawable.account_circle_24px,
                isPurchased = false,
                itemPrice = 20
            ),
            StoreItem(
                "NFT 2",
                R.drawable.account_circle_24px,
                isPurchased = false,
                itemPrice = 20
            )
        )
        storeItemDaQuyAdapter = StoreItemAdapter(storeItemList)
        storeItemNFTAdapter = StoreItemAdapter(storeItemNFTList)
        binding.rcvDaQuy.apply {
            layoutManager = LinearLayoutManager(this@StoreActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = storeItemDaQuyAdapter
        }

        binding.rcvNft.apply {
            layoutManager = LinearLayoutManager(this@StoreActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = storeItemNFTAdapter
        }


    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}