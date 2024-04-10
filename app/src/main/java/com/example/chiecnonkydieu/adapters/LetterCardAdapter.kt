package com.example.chiecnonkydieu.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import android.widget.TextView
import com.example.chiecnonkydieu.R
import com.example.chiecnonkydieu.data.LetterCard
import com.google.android.material.card.MaterialCardView

class LetterCardAdapter(val letterCardList: List<LetterCard>): RecyclerView.Adapter<LetterCardAdapter.LetterCardViewHolder>() {

    class LetterCardViewHolder(viewLetterCard: View) : RecyclerView.ViewHolder(viewLetterCard) {
        private val tvLetter: TextView = viewLetterCard.findViewById(R.id.text_card_letter)
        private val cardView: MaterialCardView = viewLetterCard.findViewById(R.id.card_view_letter_cards)

        fun bind(letterCard: LetterCard) {
            if (letterCard.letter == " ") {
                cardView.visibility = View.INVISIBLE
            } else if (!letterCard.isHidden) {
                tvLetter.text = letterCard.letter
                tvLetter.visibility = View.VISIBLE
                cardView.setCardBackgroundColor(Color.YELLOW)
                cardView.visibility = View.VISIBLE
            }
//            else if (letterCard.isHidden) {
//                tvLetter.text = letterCard.letter
//                tvLetter.visibility = View.INVISIBLE
//            }
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            : LetterCardViewHolder {
        val viewLetterCard = LayoutInflater.from(parent.context)
            .inflate(R.layout.view_letter_card, parent, false)
        return LetterCardViewHolder(viewLetterCard)
    }

    override fun onBindViewHolder(holder: LetterCardViewHolder, position: Int) {
        holder.bind(letterCardList[position])
    }

    override fun getItemCount(): Int {
        return if (letterCardList.isEmpty()) 0
        else letterCardList.size
    }

}