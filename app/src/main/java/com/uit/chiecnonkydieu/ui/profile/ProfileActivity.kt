package com.uit.chiecnonkydieu.ui.profile

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.uit.chiechnonkydieu.R
import com.uit.chiechnonkydieu.databinding.ActivityProfileBinding
import com.uit.chiecnonkydieu.ui.admin.LoginActivity

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityProfileBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val toolbar: Toolbar = binding.toolbar
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val savedAddress = loadAddress()
        binding.tvAddress.text = savedAddress

        binding.btnDangXuat.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
            finish()
        }

        binding.btnEdit.setOnClickListener {
            showEditDialog()
        }
    }

    private fun showEditDialog() {
        // Inflate the dialog layout
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_edit_address, null)
        val editText = dialogView.findViewById<EditText>(R.id.et_address)
        editText.setText(loadAddress())

        val dialog = AlertDialog.Builder(this)
            .setTitle("Edit Address")
            .setView(dialogView)
            .setPositiveButton("Save") { dialogInterface, i ->
                val newAddress = editText.text.toString()
                saveAddress(newAddress)
                binding.tvAddress.text = newAddress
                Toast.makeText(this, "New address saved: $newAddress", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancel") { dialogInterface, i ->
                dialogInterface.dismiss()
            }
            .create()

        dialog.show()
    }

    private fun saveAddress(address: String) {
        val editor = sharedPreferences.edit()
        editor.putString("address", address)
        editor.apply()
    }

    private fun loadAddress(): String {
        return sharedPreferences.getString("address", "0x262dA04adF6C48Abf80eB1D486b8235A22097447") ?: ""
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
