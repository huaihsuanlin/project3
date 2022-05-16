package com.ecs198f.foodtrucks

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ItemDao {

    @Query("SELECT * FROM FoodItem")
    suspend fun listAllItems() : List<FoodItem>

    @Query("SELECT * FROM FoodItem WHERE truckId=:truckID")
    suspend fun listAllItemsByTruck(truckID: Int): List<FoodItem>

    @Insert
    suspend fun addFoodItem(item: FoodItem)

    @Insert
    suspend fun addFoodItems(items: List<FoodItem>)

    @Delete
    suspend fun delFoodItem(item: FoodItem)
}