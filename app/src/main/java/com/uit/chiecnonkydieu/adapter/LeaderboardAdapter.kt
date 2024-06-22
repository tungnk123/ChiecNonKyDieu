package com.uit.chiecnonkydieu.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.uit.chiechnonkydieu.R
import com.uit.chiecnonkydieu.model.LeaderboardItem

class LeaderboardAdapter(
    private val list: List<LeaderboardItem>
): RecyclerView.Adapter<LeaderboardAdapter.LeaderboardViewHolder>() {


    inner class LeaderboardViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tv_name)
        val tvStt: TextView = itemView.findViewById(R.id.tv_stt)
        val tvScore: TextView = itemView.findViewById(R.id.tv_score)
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
        holder.tvName.text = leaderboardItem.name
        holder.tvStt.text = leaderboardItem.stt.toString()
        holder.tvScore.text = leaderboardItem.score.toString()

    }
}