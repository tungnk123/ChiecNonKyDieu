package com.uit.chiecnonkydieu.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.uit.chiechnonkydieu.R
import com.uit.chiecnonkydieu.callback.OnItemClickListener
import com.uit.chiecnonkydieu.data.CoinPayment

class MyAdapter(private val coinPaymentList: ArrayList<CoinPayment>, private val itemClickListener: OnItemClickListener) : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    private var selectedPosition = -1
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_coin, parent, false)
        return MyViewHolder(itemView);
    }

    override fun getItemCount(): Int {
        return coinPaymentList.size
    }
    
    override fun onBindViewHolder(holder: MyViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val currentItem = coinPaymentList[position]
        holder.amountCoin.text = currentItem.amountCoin
        holder.amountExtra.text = currentItem.extraCoin
        holder.imageCoin.setImageResource(currentItem.imageMoney)
        holder.moneyPayment.text = (currentItem.moneyPayment)
        if (currentItem.extraCoin == "0")  {
            holder.amountExtra.visibility = View.GONE;
        } else {
            holder.amountExtra.visibility = View.VISIBLE;
        }
        if (position == selectedPosition) {
            holder.layout.setBackgroundResource(R.drawable.bg_selected)
            holder.checkedImage.visibility = View.VISIBLE;
        } else {
            holder.layout.setBackgroundResource(R.drawable.bg_normal)
            holder.checkedImage.visibility = View.GONE;
        }

        holder.itemView.setOnClickListener {
            val previousPosition = selectedPosition
            selectedPosition = position

            notifyItemChanged(previousPosition)
            notifyItemChanged(selectedPosition)
            itemClickListener.onItemClick(position)
        }

    }
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val amountCoin: TextView = itemView.findViewById(R.id.tv_amountCoins)
        val amountExtra: TextView = itemView.findViewById(R.id.tv_plus);
        val imageCoin: ImageView = itemView.findViewById(R.id.img_coin);
        val moneyPayment: TextView = itemView.findViewById(R.id.tv_price);
        val layout: LinearLayout = itemView.findViewById(R.id.linearLayoutItemCoin);
        val checkedImage: ImageView = itemView.findViewById(R.id.img_itemchecked);
    }
}