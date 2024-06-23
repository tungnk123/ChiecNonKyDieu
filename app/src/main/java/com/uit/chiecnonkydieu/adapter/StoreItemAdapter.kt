package com.uit.chiecnonkydieu.adapter

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.uit.chiechnonkydieu.R
import com.uit.chiecnonkydieu.model.StoreItem

class StoreItemAdapter(
        private val storeItemList: List<StoreItem>,
        private val onCoinValueChanged: (Int) -> Unit
) : RecyclerView.Adapter<StoreItemAdapter.StoreItemViewHolder>() {

        private lateinit var sharedPreferences: SharedPreferences

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoreItemViewHolder {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.store_item, parent, false)
                return StoreItemViewHolder(view)
        }

        override fun onBindViewHolder(holder: StoreItemViewHolder, position: Int) {
                val storeItem = storeItemList[position]
                holder.imgIcon.setImageResource(storeItem.itemImg)
                holder.tvItemName.text = storeItem.itemName

                if (storeItem.isPurchased) {
                        holder.tvCoinNumber.text = "Purchased"
                        holder.imgCoin.visibility = View.GONE
                } else {
                        holder.tvCoinNumber.text = storeItem.itemPrice.toString()
                        holder.imgCoin.visibility = View.VISIBLE
                }
        }

        override fun getItemCount(): Int {
                return storeItemList.size
        }

        inner class StoreItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
                val tvCoinNumber: TextView = itemView.findViewById(R.id.tv_storeItem_coin)
                val imgIcon: ImageView = itemView.findViewById(R.id.img_storeItem_icon)
                val llCoinWrapper: LinearLayout = itemView.findViewById(R.id.ll_storeItem_iconWrapper)
                val tvItemName: TextView = itemView.findViewById(R.id.tv_storeItem_itemName)
                val imgCoin: ImageView = itemView.findViewById(R.id.img_storeItem_coin)

                init {
                        llCoinWrapper.setOnClickListener {
                                val context = itemView.context
                                showPurchaseDialog(context, adapterPosition)
                        }
                }

                private fun showPurchaseDialog(context: Context, position: Int) {
                        val builder = AlertDialog.Builder(context)
                        builder.setTitle("Confirm Buy")
                                .setMessage("Are you sure you want to buy this item?")
                                .setPositiveButton("Buy") { _, _ ->
                                        var clickedItem = storeItemList[position]
                                        sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
                                        val coin = sharedPreferences.getInt("coin", 1000)

                                        if (coin >= clickedItem.itemPrice && !clickedItem.isPurchased) {
                                                clickedItem.isPurchased = true
                                                sharedPreferences.edit().apply {
                                                        putInt("coin", coin - clickedItem.itemPrice)
                                                        apply()
                                                }
                                                tvCoinNumber.text = "Purchased"
                                                imgCoin.visibility = View.GONE
                                                onCoinValueChanged(coin - clickedItem.itemPrice)

                                        } else {
                                                Toast.makeText(context, "Not enough money! 😅😅😅😅", Toast.LENGTH_SHORT).show()
                                        }
                                }
                                .setNegativeButton("Cancel", null)
                                .show()
                }
        }
}
