package com.ecs198f.foodtrucks

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TruckDao {

    // each function represents a query to the database

    // get all trucks in the database
    @Query("SELECT * FROM FoodTruck ORDER BY id")
    suspend fun listAllTrucks() : List<FoodTruck>

    @Query("SELECT * FROM FoodTruck WHERE priceLevel=:type")
    suspend fun listTrucksofPrice(type: String): List<FoodTruck>

    @Query("DELETE FROM FoodTruck WHERE priceLevel=:type")
    suspend fun removeTrucksOfPrice(type: String)

    @Query("SELECT * FROM FoodTruck WHERE id=:id")
    suspend fun getTruckbyID(id: Int) : FoodTruck

    @Insert
    suspend fun addTruck(foodTruck: FoodTruck)

    @Insert
    suspend fun addTrucks(trucks: List<FoodTruck>)

    @Query("DELETE FROM FoodTruck")
    suspend fun deleteTrucks()

}