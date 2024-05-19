package com.example.chiecnonkydieu

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
import com.example.chiecnonkydieu.databinding.ActivityMainBinding
import com.example.chiecnonkydieu.ui.CreateRoomActivity
import com.example.chiecnonkydieu.ui.playingRoom.PlayingRoomActivity
import com.example.chiecnonkydieu.ui.SearchRoomActivity
import com.example.chiecnonkydieu.ui.admin.AddQuestionActivity
import com.example.chiecnonkydieu.ui.wheel.WheelActivity
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
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
            val intent = Intent(this, WheelActivity::class.java)
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