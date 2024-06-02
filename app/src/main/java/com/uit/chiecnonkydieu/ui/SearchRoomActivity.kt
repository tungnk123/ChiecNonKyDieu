package com.uit.chiecnonkydieu.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.lifecycleScope
import com.uit.chiechnonkydieu.R
import com.uit.chiechnonkydieu.databinding.ActivitySearchRoomBinding
import com.uit.chiecnonkydieu.data.GameData
import com.uit.chiecnonkydieu.model.Player
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class SearchRoomActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchRoomBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySearchRoomBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.search_room_main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val toolbar: Toolbar = binding.toolbar
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        binding.btnSearchRoom.setOnClickListener {
            lifecycleScope.launch {
                val isJoined = lifecycleScope.async {
                    GameData.initialize(applicationContext)
                    GameData.joinOnlineGame(
                        player = Player(name = binding.edtName.text.toString()),
                        gameId = binding.edtMaPhong.text.toString().toInt()
                    )
                }.await()

                if (isJoined) {
                    saveCurrentPlayerToPreferencesDataStore(binding.edtName.text.toString())
                    goToWaitingRooom()
                } else {
                    Toast.makeText(applicationContext, "Error when connect game", Toast.LENGTH_LONG).show()
                }
            }

        }
    }

    private fun saveCurrentPlayerToPreferencesDataStore(currentPlayer: String) {
        lifecycleScope.launch {
            dataStore.edit { settings ->
                settings[CURRENT_PLAYER] = currentPlayer
            }
            val savedPlayer = dataStore.data.first()[CURRENT_PLAYER]
            Log.d("DataStore", "Saved player from search: $savedPlayer")
        }
    }

    fun goToWaitingRooom() {
        try {
            val intent = Intent(this, WaitingRoomActivity::class.java)
            intent.putExtra("room_id", binding.edtMaPhong.text.toString())
            startActivity(intent)
        }
        catch (exception: Exception) {
            Toast.makeText(this, binding.edtMaPhong.text, Toast.LENGTH_LONG).show()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}