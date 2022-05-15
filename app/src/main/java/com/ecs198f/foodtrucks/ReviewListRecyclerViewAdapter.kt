package com.ecs198f.foodtrucks

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.ecs198f.foodtrucks.databinding.FoodItemBinding
import com.ecs198f.foodtrucks.databinding.ReviewBinding

class ReviewListRecyclerViewAdapter(private var items: List<Review>) :
    RecyclerView.Adapter<ReviewListRecyclerViewAdapter.ViewHolder>(){
    class ViewHolder(val binding: ReviewBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = ReviewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ReviewListRecyclerViewAdapter.ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReviewListRecyclerViewAdapter.ViewHolder, position: Int) {
        items[position].let {
            holder.binding.apply {
                Glide.with(root).load(it.authorAvatarUrl).into(authorAvatar)
                authorName.text = it.authorName
                reviewContent.text = it.content
            }
        }
    }

    override fun getItemCount() = items.size

    fun updateReviews(items: List<Review>) {
        this.items = items
        notifyDataSetChanged()
    }
}