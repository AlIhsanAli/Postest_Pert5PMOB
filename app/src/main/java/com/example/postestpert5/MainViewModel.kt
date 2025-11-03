package com.example.postestpert5

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    private val _posts = MutableLiveData<MutableList<Post>>().apply { value = mutableListOf() }
    val posts: LiveData<MutableList<Post>> = _posts

    fun addPost(post: Post) {
        val currentPosts = _posts.value ?: mutableListOf()
        currentPosts.add(0, post)
        _posts.value = currentPosts
    }

    fun updatePost(updatedPost: Post) {
        val currentPosts = _posts.value
        val index = currentPosts?.indexOfFirst { it.id == updatedPost.id }
        if (index != null && index != -1) {
            currentPosts[index] = updatedPost
            _posts.value = currentPosts
        }
    }

    fun deletePost(post: Post) {
        val currentPosts = _posts.value
        currentPosts?.remove(post)
        _posts.value = currentPosts
    }
}