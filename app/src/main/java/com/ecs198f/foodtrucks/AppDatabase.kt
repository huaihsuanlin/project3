package com.ecs198f.foodtrucks

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import java.time.LocalDate
import java.time.LocalDateTime

@Database(entities = [FoodItem::class, FoodTruck::class], version = 1)
@TypeConverters(AppDatabase.Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun truckDao() : TruckDao
    abstract fun itemDao() : ItemDao

    class Converters {
        @TypeConverter
        fun fromString (s: String) : LocalDateTime = LocalDateTime.parse(s)
        @TypeConverter
        fun toString (ldt: LocalDateTime) : String = ldt.toString();
    }
}