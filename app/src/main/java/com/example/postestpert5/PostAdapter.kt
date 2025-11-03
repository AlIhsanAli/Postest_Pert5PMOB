package com.example.postestpert5

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.postestpert5.databinding.ItemPostBinding

class PostAdapter(
    private var postList: MutableList<Post>,
    private val onEdit: (Post) -> Unit,
    private val onDelete: (Post) -> Unit
) : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    inner class PostViewHolder(private val binding: ItemPostBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(post: Post) {
            binding.ivPostProfile.setImageResource(post.profileImage)
            binding.tvPostUsername.text = post.username
            binding.ivPostImage.load(post.imageUri)
            binding.tvPostCaption.text = post.caption

            binding.ivPostOptions.setOnClickListener { view ->
                val popup = PopupMenu(view.context, view)
                popup.menu.add("Edit").setOnMenuItemClickListener { onEdit(post); true }
                popup.menu.add("Delete").setOnMenuItemClickListener { onDelete(post); true }
                popup.show()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = ItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding)
    }

    override fun getItemCount(): Int = postList.size

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(postList[position])
    }

    fun updateData(newPosts: List<Post>) {
        postList.clear()
        postList.addAll(newPosts)
        notifyDataSetChanged()
    }
}