package com.ecs198f.foodtrucks

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [FoodItem::class, FoodTruck::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun truckDao() : TruckDao
    abstract fun itemDao() : ItemDao
}