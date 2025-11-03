package com.example.postestpert5

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.postestpert5.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var postAdapter: PostAdapter

    private val profileImages = listOf(
        R.drawable.monyet,
        R.drawable.anjing,
        R.drawable.kucing,
        R.drawable.bebek,
        R.drawable.bahlil
    )
    private var profileImageCounter = 0

    private val addPostLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.let { intent ->
                val imageUriString = intent.getStringExtra("image_uri")
                val username = intent.getStringExtra("username")
                val caption = intent.getStringExtra("caption")

                if (imageUriString != null && username != null && caption != null) {
                    val imageUri = Uri.parse(imageUriString)
                    val profileImageRes = profileImages[profileImageCounter % profileImages.size]
                    profileImageCounter++

                    val newPost = Post(
                        username = username,
                        imageUri = imageUri,
                        caption = caption,
                        profileImage = profileImageRes
                    )
                    mainViewModel.addPost(newPost)
                }
            }
        }
    }

    private val editPostLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.let { intent ->
                val postId = intent.getStringExtra("post_id")
                val updatedUsername = intent.getStringExtra("updated_username")
                val updatedCaption = intent.getStringExtra("updated_caption")
                val updatedImageUriString = intent.getStringExtra("updated_image_uri")

                if (postId != null && updatedUsername != null && updatedCaption != null && updatedImageUriString != null) {
                    val updatedImageUri = Uri.parse(updatedImageUriString)
                    val currentPost = mainViewModel.posts.value?.find { it.id == postId }
                    currentPost?.let {
                        val updatedPost = it.copy(
                            username = updatedUsername,
                            caption = updatedCaption,
                            imageUri = updatedImageUri
                        )
                        mainViewModel.updatePost(updatedPost)
                        Toast.makeText(this, "Postingan berhasil diperbarui", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupStoryRecyclerView()
        setupPostRecyclerView()

        binding.fabAddPost.setOnClickListener {
            val intent = Intent(this, AddPostActivity::class.java)
            addPostLauncher.launch(intent)
        }

        mainViewModel.posts.observe(this) { posts ->
            postAdapter.updateData(posts)
        }
    }

    private fun setupStoryRecyclerView() {
        val stories = listOf(
            Story("user1", R.drawable.monyet),
            Story("user2", R.drawable.anjing),
            Story("user3", R.drawable.kucing),
            Story("user4", R.drawable.bebek),
            Story("user5", R.drawable.bahlil)
        )
        val storyAdapter = StoryAdapter(stories)
        binding.rvStories.apply {
            adapter = storyAdapter
            layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun setupPostRecyclerView() {
        postAdapter = PostAdapter(
            mutableListOf(),
            onEdit = { post ->
                val intent = Intent(this, EditPostActivity::class.java).apply {
                    putExtra("post_id", post.id)
                    putExtra("username", post.username)
                    putExtra("caption", post.caption)
                    putExtra("image_uri", post.imageUri.toString())
                }
                editPostLauncher.launch(intent)
            },
            onDelete = { post -> showDeleteConfirmationDialog(post) }
        )
        binding.rvPosts.apply {
            adapter = postAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }
    }

    private fun showDeleteConfirmationDialog(post: Post) {
        AlertDialog.Builder(this)
            .setTitle("Hapus Post")
            .setMessage("Apakah Anda yakin ingin menghapus post ini?")
            .setPositiveButton("Hapus") { _, _ ->
                mainViewModel.deletePost(post)
                Toast.makeText(this, "Post dihapus", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Batal", null)
            .show()
    }
}