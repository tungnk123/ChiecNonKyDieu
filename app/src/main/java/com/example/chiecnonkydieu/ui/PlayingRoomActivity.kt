package com.example.chiecnonkydieu.ui

import android.content.DialogInterface
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.example.chiecnonkydieu.R
import com.example.chiecnonkydieu.data.GameData
import com.example.chiecnonkydieu.data.GameModel
import com.example.chiecnonkydieu.data.GameStatus
import com.example.chiecnonkydieu.databinding.ActivityPlayingRoomBinding
import kotlinx.coroutines.launch


class PlayingRoomActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPlayingRoomBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityPlayingRoomBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        lifecycleScope.launch {
            GameData.fetchGameModel(intent.getStringExtra("room_id").toString().toInt())

        }

        GameData.gameModel.observe(this, Observer { gameModel ->
            updateUi(gameModel)
        })

        binding.btnDoan.setOnClickListener {
            Toast.makeText(this, "Doan click", Toast.LENGTH_LONG).show()
            if (binding.edtDoan.text.toString() == "A") {
                // TODO
            }
        }

        binding.btnGiai.setOnClickListener {
            Toast.makeText(this, "Giai click", Toast.LENGTH_LONG).show()
            showDialog()
        }

        binding.iconButton.setOnClickListener {
            Toast.makeText(this, "Hint click", Toast.LENGTH_LONG).show()
        }
    }

    private fun updateUi(gameModel: GameModel?) {
        if (gameModel != null) {
            gameModel.gameStatus = GameStatus.INPROGRESS

            updatePlayerList(gameModel)
            updateCurrentQuestion(gameModel)
        }

    }

    private fun showDialog() {
        var m_Text: String = ""
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Nhập lời giải cho câu hỏi")
        val viewInflated = LayoutInflater.from(this).inflate(R.layout.dialog_edittext, null, false)
        val input = viewInflated.findViewById<EditText>(R.id.input)
        builder.setView(viewInflated)

        builder.setPositiveButton(android.R.string.ok) { dialog, which ->
            dialog.dismiss()
            m_Text = input.text.toString()
        }

        builder.setNegativeButton(android.R.string.cancel) { dialog, which ->
            dialog.cancel()
        }

        builder.show()


    }

    private fun updateCurrentQuestion(gameModel: GameModel) {
        binding.tvQuestion.text = gameModel.currentQuestion
    }

    private fun updatePlayerList(gameModel: GameModel) {
        when (gameModel.playersList.size) {
            1 -> {
                // player1
                val colorResId = R.color.orange
                val backgroundColor = ContextCompat.getColor(this, colorResId)
                binding.cvPlayer1.setCardBackgroundColor(backgroundColor)
                binding.imgLogo1.setImageResource(R.drawable.ic_man)
                binding.tvPlayerName1.text = gameModel.playersList[0].name
                binding.tvScore1.text = gameModel.playersList[0].score.toString()
            }
            2 -> {
                // player1 and player 2
                val colorResId = R.color.orange
                val backgroundColor = ContextCompat.getColor(this, colorResId)
                binding.imgLogo1.setImageResource(R.drawable.ic_man)
                binding.imgLogo2.setImageResource(R.drawable.ic_man)
                binding.tvPlayerName1.text = gameModel.playersList[0].name
                binding.tvPlayerName2.text = gameModel.playersList[1].name

                binding.tvScore1.text = gameModel.playersList[0].score.toString()
                binding.tvScore2.text = gameModel.playersList[1].score.toString()


            }

            3 -> {
                // 3 player
                val colorResId = R.color.orange
                val backgroundColor = ContextCompat.getColor(this, colorResId)
                binding.imgLogo1.setImageResource(R.drawable.ic_man)
                binding.imgLogo2.setImageResource(R.drawable.ic_man)
                binding.imgLogo3.setImageResource(R.drawable.ic_man)
                binding.tvPlayerName1.text = gameModel.playersList[0].name
                binding.tvPlayerName2.text = gameModel.playersList[1].name
                binding.tvPlayerName3.text = gameModel.playersList[2].name

                binding.tvScore1.text = gameModel.playersList[0].score.toString()
                binding.tvScore2.text = gameModel.playersList[1].score.toString()
                binding.tvScore3.text = gameModel.playersList[2].score.toString()
            }
        }
    }
}