package com.uit.chiecnonkydieu.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.uit.chiechnonkydieu.R
import com.uit.chiecnonkydieu.model.LeaderboardItem

class LeaderboardAdapter(
    private var list: List<LeaderboardItem>
): RecyclerView.Adapter<LeaderboardAdapter.LeaderboardViewHolder>() {


    inner class LeaderboardViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tv_name)
        val tvStt: TextView = itemView.findViewById(R.id.tv_stt)
        val tvScore: TextView = itemView.findViewById(R.id.tv_score)
        val cvWrapper: CardView = itemView.findViewById(R.id.cv_leaderboardItem)
    }
    fun updateData(newItems: List<LeaderboardItem>) {
        list = newItems
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeaderboardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_leaderboard, parent, false)
        return LeaderboardViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: LeaderboardViewHolder, position: Int) {
        val leaderboardItem = list[position]

        if (position == 0) {
            holder.tvStt.setBackgroundResource(R.drawable.ic_medal1)
            holder.cvWrapper.setCardBackgroundColor(Color.YELLOW)
        }
        else if (position == 1) {
            holder.tvStt.setBackgroundResource(R.drawable.ic_medal2)
            holder.cvWrapper.setCardBackgroundColor(Color.GRAY)
        }
        else if (position == 2) {
            holder.tvStt.setBackgroundResource(R.drawable.ic_medal3)
            holder.cvWrapper.setCardBackgroundColor(Color.rgb(254, 193, 78))
        }
        else {
            holder.tvStt.text = leaderboardItem.stt.toString()
        }
        holder.tvName.text = leaderboardItem.name
        holder.tvScore.text = leaderboardItem.score.toString()

    }
}