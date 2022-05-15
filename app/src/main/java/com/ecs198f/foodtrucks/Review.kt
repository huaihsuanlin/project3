package com.ecs198f.foodtrucks

data class Review(
    val id: String,
    val truckId: String,
    val authorName: String,
    val authorAvatarUrl: String,
    val content: String,
    val imageUrls: List<String>
)
