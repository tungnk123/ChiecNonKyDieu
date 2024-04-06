package com.example.chiecnonkydieu.ui

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.example.chiecnonkydieu.R
import com.example.chiecnonkydieu.data.GameData
import com.example.chiecnonkydieu.data.GameModel
import com.example.chiecnonkydieu.databinding.ActivityWaitingRoomBinding
import kotlinx.coroutines.launch

class WaitingRoomActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWaitingRoomBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityWaitingRoomBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.waiting_room_main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val toolbar: Toolbar = binding.toolbar
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)


        binding.tvMaPhong.text = intent.getStringExtra("room_id")
//      fetch data from firebase
        lifecycleScope.launch {

            GameData.fetchGameModel(intent.getStringExtra("room_id").toString().toInt())

        }

        // update ui with the new model
        GameData.gameModel.observe(this, Observer { gameModel ->
            updateUi(gameModel)
        })


    }

    private fun updateUi(gameModel: GameModel?) {
        if (gameModel != null) {
            when (gameModel.playersList.size) {
                1 -> {
                    // player1
                    val colorResId = R.color.orange
                    val backgroundColor = ContextCompat.getColor(this, colorResId)
                    binding.cvPlayer1.setCardBackgroundColor(backgroundColor)
                    binding.imgLogo1.setImageResource(R.drawable.ic_man)
                    binding.tvPlayerName1.text = gameModel.playersList[0].name
                }
                2 -> {
                    // player1 and player 2
                    val colorResId = R.color.orange
                    val backgroundColor = ContextCompat.getColor(this, colorResId)
                    binding.cvPlayer1.setCardBackgroundColor(backgroundColor)
                    binding.cvPlayer2.setCardBackgroundColor(backgroundColor)
                    binding.imgLogo1.setImageResource(R.drawable.ic_man)
                    binding.imgLogo2.setImageResource(R.drawable.ic_man)
                    binding.tvPlayerName1.text = gameModel.playersList[0].name
                    binding.tvPlayerName2.text = gameModel.playersList[1].name

                }

                3 -> {
                    // 3 player
                    val colorResId = R.color.orange
                    val backgroundColor = ContextCompat.getColor(this, colorResId)
                    binding.cvPlayer1.setCardBackgroundColor(backgroundColor)
                    binding.cvPlayer2.setCardBackgroundColor(backgroundColor)
                    binding.cvPlayer3.setCardBackgroundColor(backgroundColor)
                    binding.imgLogo1.setImageResource(R.drawable.ic_man)
                    binding.imgLogo2.setImageResource(R.drawable.ic_man)
                    binding.imgLogo3.setImageResource(R.drawable.ic_man)
                    binding.tvPlayerName1.text = gameModel.playersList[0].name
                    binding.tvPlayerName2.text = gameModel.playersList[1].name
                    binding.tvPlayerName3.text = gameModel.playersList[2].name
                }
            }

        }

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
