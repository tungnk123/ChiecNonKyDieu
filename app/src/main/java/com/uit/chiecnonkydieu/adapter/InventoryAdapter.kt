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
import coil.load
import com.uit.chiechnonkydieu.R
import com.uit.chiecnonkydieu.model.StoreItem
import com.uit.chiecnonkydieu.ui.inventory.InventoryViewModel
import java.util.Objects

class InventoryAdapter(
    private var storeItemList: List<StoreItem>,
    private val username: String,
    private val inventoryViewModel: InventoryViewModel,
) : RecyclerView.Adapter<InventoryAdapter.StoreItemViewHolder>() {

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoreItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.inventory_item, parent, false)
        return StoreItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: StoreItemViewHolder, position: Int) {
        val storeItem = storeItemList[position]
        if (storeItem.itemImg.isEmpty()) {
            storeItem.itemImg = "https://cdn-icons-png.flaticon.com/512/6411/6411782.png"
        }
        holder.imgIcon.load(
            storeItem.itemImg
        ) {
            placeholder(R.drawable.ic_placeholder)
            error(R.drawable.ic_error)
        }
        holder.tvItemName.text = storeItem.itemName
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
    fun updateItems(newItems: List<StoreItem>) {
        storeItemList = newItems
        notifyDataSetChanged()
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
                    val clickedItem = storeItemList[position]
                    inventoryViewModel.sellItems(username, "123456", clickedItem.itemId, 1, clickedItem.itemPrice)
                    Toast.makeText(context, "Item sold!", Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }
}
