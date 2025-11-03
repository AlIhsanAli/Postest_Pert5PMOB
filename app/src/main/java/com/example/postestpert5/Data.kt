package com.example.postestpert5

import android.net.Uri
import java.util.UUID

data class Story(
    val username: String,
    val profileImage: Int
)

data class Post(
    val id: String = UUID.randomUUID().toString(),
    var username: String,
    var imageUri: Uri?,
    var caption: String,
    val profileImage: Int
)