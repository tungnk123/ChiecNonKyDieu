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
import java.util.Objects

class InventoryAdapter(
    private val storeItemList: List<StoreItem>,
) : RecyclerView.Adapter<InventoryAdapter.StoreItemViewHolder>() {

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoreItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.inventory_item, parent, false)
        return StoreItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: StoreItemViewHolder, position: Int) {
        val storeItem = storeItemList[position]
        holder.imgIcon.setImageResource(storeItem.itemImg)
        holder.tvItemName.text = storeItem.itemName

//        if (storeItem.isPurchased) {
//            holder.tvCoinNumber.text = "Purchased"
//            holder.imgCoin.visibility = View.GONE
//        } else {
//            holder.tvCoinNumber.text = storeItem.itemPrice.toString()
//            holder.imgCoin.visibility = View.VISIBLE
//        }
    }

    override fun getItemCount(): Int {
        return storeItemList.size
    }

    inner class StoreItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgIcon: ImageView = itemView.findViewById(R.id.img_storeItem_icon)
        val llCoinWrapper: LinearLayout = itemView.findViewById(R.id.ll_storeItem_iconWrapper)
        val tvItemName: TextView = itemView.findViewById(R.id.tv_storeItem_itemName)

        init {
            llCoinWrapper.setOnClickListener {
                val context = itemView.context
                showPurchaseDialog(context, adapterPosition)
            }
        }

        private fun showPurchaseDialog(context: Context, position: Int) {
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Confirm Buy")
                .setMessage("Are you sure you want to sell this item?")
                .setPositiveButton("Sell") { _, _ ->
                    Toast.makeText(context, "Feature is under construction", Toast.LENGTH_SHORT).show()
//                    var clickedItem = storeItemList[position]
//                    clickedItem.isPurchased = true
//                    sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
//                    val coin = sharedPreferences.getInt("coin", 1000)
//                    sharedPreferences.edit().apply {
//                        putInt("coin", coin - clickedItem.itemPrice)
//                        apply()
//                    }
//                                        if (!clickedItem.isPurchased) {
////                                                val currentCoins = coinManager.getData("Coin", 100)
////                                                if (currentCoins >= clickedItem.itemPrice) {
////                                                        clickedItem.isPurchased = true
////                                                        storeItemViewModel.updateItemPurchased(clickedItem.item_id, true)
////                                                        tvCoinNumber.text = "Purchased"
////                                                        imgCoin.visibility = View.GONE
////                                                        val newMoney = currentCoins - clickedItem.itemPrice
////                                                        coinManager.saveData("Coin", newMoney)
////                                                        StoreFragment.storeNewMoney()
////
////                                                        if (Objects.equals(clickedItem.storeItemType, "tree")) {
////                                                                HabitDataBase.getInstance(context).habitDAO()
////                                                                        .updateTreeForestIsPurchased(clickedItem.item_id, true)
////                                                                notifyDataSetChanged()
////                                                        }
//                                                } else {
//                                                        Toast.makeText(context, "Not enough money! 😅😅😅😅", Toast.LENGTH_SHORT).show()
//                                                }
//                                        } else {
//                                                Toast.makeText(context, "Item is already purchased!", Toast.LENGTH_SHORT).show()
//                                        }
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }
}
