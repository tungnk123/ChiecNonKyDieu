package com.uit.chiecnonkydieu.ui.store

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.uit.chiechnonkydieu.R
import com.uit.chiechnonkydieu.databinding.ActivityStoreBinding
import com.uit.chiecnonkydieu.AppContainer
import com.uit.chiecnonkydieu.adapter.StoreItemAdapter
import com.uit.chiecnonkydieu.model.GemResponse
import com.uit.chiecnonkydieu.model.ItemMarketDto
import com.uit.chiecnonkydieu.model.MintResponse
import com.uit.chiecnonkydieu.model.StoreItem
import com.uit.chiecnonkydieu.model.toStoreItem
import com.uit.chiecnonkydieu.network.MintRequest
import com.uit.chiecnonkydieu.ui.Payment.PaymentActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StoreActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStoreBinding

    private lateinit var storeItemDaQuyAdapter: StoreItemAdapter
    private lateinit var storeItemNFTAdapter: StoreItemAdapter
    private lateinit var storeItemList: List<StoreItem>
    private lateinit var storeItemNFTList: List<StoreItem>
    private lateinit var sharedPreferences: SharedPreferences
    val appContainer: AppContainer = AppContainer()
    val storeViewModel: StoreViewModel by viewModels<StoreViewModel>()

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

//        storeItemList = listOf(
//            StoreItem(
//                "Đá quý 1",
//                "https://picsum.photos/536/354",
//                isPurchased = false,
//                itemPrice = 20
//            ),
//            StoreItem(
//                "Đá quý 2",
//                "https://picsum.photos/536/354",
//                isPurchased = false,
//                itemPrice = 20
//            ),
//            StoreItem(
//                "Đá quý 3",
//                "https://picsum.photos/536/354",
//                isPurchased = false,
//                itemPrice = 20
//            )
//        )
        storeItemNFTList = listOf(
            StoreItem(
                "βetagem 127",
                "https://i.seadn.io/gae/JkxC-o3FiNx7_2tCEkWkM41RoBds-4Ey5GiUobg3bi7jyuKnT3gIxgXgdG4PY6x01nSxI3RtKbPUHQqr22BTI78-tg?auto=format&dpr=1&w=1000",
                isPurchased = false,
                itemPrice = 20
            ),
            StoreItem(
                "βetagem 139",
                "https://i.seadn.io/gae/SrQE9XAc3mbTIGd_Oh-_RyQIn4_AE485wBLORYPT5QpRMyP5uASun_WnyTxuoLgZTQnqVwwJokbgCCXebFCd7iWk?auto=format&dpr=1&w=1000",
                isPurchased = false,
                itemPrice = 40
            ),
            StoreItem(
                "βetagem 268",
                "https://i.seadn.io/gae/G5_lDyTi7a-X9toCZsdFcBIwenJYfLSJO_26FNjxhWS63DU5xxE3cD1scul8zWbKxuVi4NTYoZHnYNrhGmJmj54?auto=format&dpr=1&w=1000",
                isPurchased = false,
                itemPrice = 100
            ),
            StoreItem(
                "βetagem 269",
                "https://i.seadn.io/gae/6zj2vkEhDWkXCMExFoZMe0AJKeRGZHe9l8Pxur39JPQLN-8mgV57OtaoY2wCGab5UHLBfGFSXK2UOuzU3qCh-RJB?auto=format&dpr=1&w=512",
                isPurchased = true,
                itemPrice = 50
            ),
            StoreItem(
                "βetagem 85",
                "https://i.seadn.io/gae/Twu_vxEAAHFsyxCLEGKXUtFugwCdMr6k_yQk4vo5fRZtB5IowQg33phIWw89Sx9MEBzy8D5WfO-0h3AU3ARLePj9?auto=format&dpr=1&w=1000",
                isPurchased = false,
                itemPrice = 30
            )
        )

        sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        storeItemDaQuyAdapter = StoreItemAdapter(emptyList(), ::updateCoinValue)
        storeViewModel.itemMarketDto.observe(this, Observer { gemResponse ->
            gemResponse?.let {
                updateUI(it)
            }
        })
        storeViewModel.getStoreItems()
        storeItemNFTAdapter = StoreItemAdapter(storeItemNFTList, ::updateCoinValue)
        binding.rcvDaQuy.apply {
            layoutManager = LinearLayoutManager(this@StoreActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = storeItemDaQuyAdapter
        }


        binding.rcvNft.apply {
            layoutManager = LinearLayoutManager(this@StoreActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = storeItemNFTAdapter
        }

        binding.btnNap.setOnClickListener {
            val intent = Intent(this, PaymentActivity::class.java)
            startActivity(intent)
        }


    }

    private fun updateUI(gemResponse: List<ItemMarketDto>) {
        storeItemList = gemResponse.map {
            it.toStoreItem()
        }
        Log.d("store api", gemResponse.toString())
        storeItemDaQuyAdapter.updateItems(storeItemList)
    }
    fun showMintDialog(walletAddress: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Congratulations!")
        builder.setMessage("You won an NFT! Would you like to mint NFT to your address?")

        builder.setPositiveButton("Mint") { dialog, which ->
            // Mint another NFT
            val uuid = "82d00203-738e-4136-8514-7a8aac192bd9"
            val authorization =
                "Basic NWM3MDYwNDYtYzg3My00MjI4LTk5MWEtMDZhMDM3MTU3ZGNlOjZkNGlqSTdSOVVkMA=="
            val request = MintRequest(walletAddress, 1)

            try {
                appContainer.api.mintNFT(uuid, authorization, request).enqueue(object :
                    Callback<MintResponse> {
                    override fun onResponse(
                        call: Call<MintResponse>,
                        response: Response<MintResponse>
                    ) {
                        if (response.isSuccessful) {
                            val mintResponse = response.body()
                            if (mintResponse != null && mintResponse.data.success) {
                                Toast.makeText(
                                    this@StoreActivity,
                                    "Minting successful! Transaction hash: ${mintResponse.data.transactionHash}",
                                    Toast.LENGTH_LONG
                                ).show()
                            } else {
                                Toast.makeText(
                                    this@StoreActivity,
                                    "Minting failed: ${response.message()}",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        } else {
                            Toast.makeText(
                                this@StoreActivity,
                                "Minting failed: ${response.message()}",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<MintResponse>, t: Throwable) {
                        Toast.makeText(
                            this@StoreActivity,
                            "Minting error: ${t.message}",
                            Toast.LENGTH_LONG
                        ).show()
                        Log.d("Api", t.message.toString())
                    }
                })
            } catch (e: Exception) {
                Log.d("Api", e.message.toString())
            }
        }

        builder.setNegativeButton("Cancel") { dialog, which ->
            dialog.dismiss()
        }

        builder.show()
    }

    override fun onStart() {
        super.onStart()
        updateCoinValue(sharedPreferences.getInt("coin", 1000))
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun updateCoinValue(newCoinValue: Int) {
        binding.tvCoin.text = newCoinValue.toString()
    }
}
