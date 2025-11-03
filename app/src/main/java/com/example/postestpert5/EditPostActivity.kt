package com.example.postestpert5

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.example.postestpert5.databinding.ActivityEditPostBinding

class EditPostActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditPostBinding
    private var selectedImageUri: Uri? = null
    private var postId: String? = null

    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri ->
                selectedImageUri = uri
                binding.ivEditPostImage.load(uri)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarEditPost)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbarEditPost.setNavigationOnClickListener { finish() }

        // Load existing post data
        postId = intent.getStringExtra("post_id")
        val username = intent.getStringExtra("username")
        val caption = intent.getStringExtra("caption")
        val imageUriString = intent.getStringExtra("image_uri")
        selectedImageUri = Uri.parse(imageUriString)

        binding.etEditUsername.setText(username)
        binding.etEditCaption.setText(caption)
        binding.ivEditPostImage.load(selectedImageUri)

        binding.cardImageContainer.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK).apply {
                type = "image/*"
            }
            pickImageLauncher.launch(intent)
        }

        binding.btnUpdatePost.setOnClickListener {
            val updatedUsername = binding.etEditUsername.text.toString().trim()
            val updatedCaption = binding.etEditCaption.text.toString().trim()

            if (updatedUsername.isEmpty() || updatedCaption.isEmpty()) {
                Toast.makeText(this, "Username dan caption tidak boleh kosong", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val resultIntent = Intent().apply {
                putExtra("post_id", postId)
                putExtra("updated_username", updatedUsername)
                putExtra("updated_caption", updatedCaption)
                putExtra("updated_image_uri", selectedImageUri.toString())
            }
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }
}