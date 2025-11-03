package com.example.postestpert5

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.example.postestpert5.databinding.ActivityAddPostBinding

class AddPostActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddPostBinding
    private var selectedImageUri: Uri? = null

    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri ->
                selectedImageUri = uri
                binding.ivAddPostImage.load(uri)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarAddPost)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbarAddPost.setNavigationOnClickListener { finish() }

        binding.cardImageContainer.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK).apply {
                type = "image/*"
            }
            pickImageLauncher.launch(intent)
        }

        binding.btnSavePost.setOnClickListener {
            val username = binding.etAddUsername.text.toString().trim()
            val caption = binding.etAddCaption.text.toString().trim()

            if (selectedImageUri == null) {
                Toast.makeText(this, "Pilih foto terlebih dahulu", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (username.isEmpty()) {
                Toast.makeText(this, "Username tidak boleh kosong", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (caption.isEmpty()) {
                Toast.makeText(this, "Caption tidak boleh kosong", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val resultIntent = Intent().apply {
                putExtra("image_uri", selectedImageUri.toString())
                putExtra("username", username)
                putExtra("caption", caption)
            }
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }
}