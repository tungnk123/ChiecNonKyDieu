package com.uit.chiecnonkydieu

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import coil.imageLoader
import com.uit.chiecnonkydieu.ui.CreateRoomActivity
import com.uit.chiecnonkydieu.ui.playingRoom.PlayingRoomActivity
import com.uit.chiecnonkydieu.ui.SearchRoomActivity
import com.uit.chiecnonkydieu.ui.wheel.WheelActivity
import com.google.android.material.snackbar.Snackbar
import com.uit.chiechnonkydieu.R
import com.uit.chiechnonkydieu.databinding.ActivityMainBinding
import com.uit.chiecnonkydieu.ui.inventory.InventoryActivity
import com.uit.chiecnonkydieu.ui.leaderboard.LeaderboardActivity
import com.uit.chiecnonkydieu.ui.profile.ProfileActivity
import com.uit.chiecnonkydieu.ui.store.StoreActivity
import com.uit.chiecnonkydieu.ui.wheel.EveryDayWheelActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = com.uit.chiechnonkydieu.databinding.ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        if (!isNetworkAvailable()) {
            showNoInternetMessage()
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

//        // Write a message to the database
//        val database = Firebase.database
//        val myRef = database.getReference("message")
//
//        myRef.setValue("Hello, World!")
//
//        val intent = Intent(this, WaitingRoomActivity::class.java)
//        startActivity(intent)
        binding.btnBatDau.setOnClickListener {
//            goToPlayingRoom()
            val intent = Intent(this, EveryDayWheelActivity::class.java)
            startActivity(intent)
        }

        binding.btnTaoPhong.setOnClickListener {
            val intent = Intent(this, CreateRoomActivity::class.java)
            startActivity(intent)
        }

        binding.btnTimPhong.setOnClickListener {
            val intent = Intent(this, SearchRoomActivity::class.java)
            startActivity(intent)
        }

        binding.llProfile.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }

        binding.llStore.setOnClickListener {
            val intent = Intent(this, StoreActivity::class.java)
            startActivity(intent)
        }

        binding.llInventory.setOnClickListener {
            val intent = Intent(this, InventoryActivity::class.java)
            startActivity(intent)
        }

        binding.llScoreboard.setOnClickListener {
            val intent = Intent(this, LeaderboardActivity::class.java)
            startActivity(intent)
        }
    }
    private fun goToPlayingRoom() {
        // TODO Set cung
        val intent = Intent(this, PlayingRoomActivity::class.java)
        intent.putExtra("room_id", "7232")
        startActivity(intent)
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

    private fun showNoInternetMessage() {
        val rootView: View = findViewById(android.R.id.content)
        val snackbar = Snackbar.make(
            rootView,
            "Không có kết nối internet",
            Snackbar.LENGTH_INDEFINITE
        )
        snackbar.view.setBackgroundColor(ContextCompat.getColor(this, R.color.orange))
        snackbar.show()
    }
}