package com.ecs198f.foodtrucks

import com.google.gson.annotations.SerializedName

//data class Review(
//    val id: String,
//    val truckId: String,
//    val authorName: String,
//    val authorAvatarUrl: String,
//    var content: String,
//    val imageUrls: List<String>?
//)
data class Review(
    val authorName: String,
    val authorAvatarUrl: String,
    var content: String,
)
