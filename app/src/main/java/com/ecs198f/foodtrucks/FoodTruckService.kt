package com.ecs198f.foodtrucks

import retrofit2.Call
import retrofit2.http.*

interface FoodTruckService {
    @GET("food-trucks")
    fun listFoodTrucks(): Call<List<FoodTruck>>

    @GET("food-trucks/{id}/items")
    fun listFoodItems(@Path("id") truckId: String): Call<List<FoodItem>>

    @GET("food-trucks/{truckId}/reviews")
    fun listReviews(@Path("truckId") truckId: String): Call<List<Review>>

    @POST("/food-trucks/{truckId}/reviews")
    fun createFoodReview(@Header("authorHeader") authorHeader: String,
                         @Path("truckId") truckId: String,
                         @Body reviewContent: Review,
    ): Call<Unit>

}