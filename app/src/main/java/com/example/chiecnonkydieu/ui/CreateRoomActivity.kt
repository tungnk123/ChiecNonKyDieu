package com.example.chiecnonkydieu.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.chiecnonkydieu.R
import com.example.chiecnonkydieu.data.GameData
import com.example.chiecnonkydieu.data.GameModel
import com.example.chiecnonkydieu.data.GameStatus
import com.example.chiecnonkydieu.data.Player
import com.example.chiecnonkydieu.databinding.ActivityCreateRoomBinding
import kotlinx.coroutines.launch
import kotlin.random.Random

class CreateRoomActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateRoomBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCreateRoomBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.create_room_main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val toolbar: Toolbar = binding.toolbar
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)


        binding.tvMaPhong.text = (1000 .. 9999).random().toString()
        binding.btnCreateRoom.setOnClickListener {
            lifecycleScope.launch {
                GameData.initialize(applicationContext)
                val gameModel: GameModel = GameModel(
                    gameStatus = GameStatus.CREATED,
                    gameId = binding.tvMaPhong.text.toString().toInt(),
                )
                gameModel.playersList.add(Player(binding.edtName.text.toString()))
                GameData.saveGameModel(
                    gameModel
                )

            }
            goToWaitingRooom()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    fun goToWaitingRooom() {
        val intent = Intent(this, WaitingRoomActivity::class.java)
        intent.putExtra("room_id", binding.tvMaPhong.text.toString())
        startActivity(intent)
    }
}