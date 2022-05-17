package com.ecs198f.foodtrucks

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.room.Room
import com.ecs198f.foodtrucks.databinding.ActivityMainBinding
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type
import java.time.LocalDateTime



class MainActivity : AppCompatActivity() {

    lateinit var db: AppDatabase
    lateinit var TruckDao : TruckDao
    lateinit var ItemDao : ItemDao

    private val gson = GsonBuilder()
        .registerTypeAdapter(LocalDateTime::class.java, object : JsonDeserializer<LocalDateTime> {
            override fun deserialize(
                json: JsonElement?,
                typeOfT: Type?,
                context: JsonDeserializationContext?
            ): LocalDateTime {
                return LocalDateTime.parse(json!!.asString)
            }
        })
        .create()

    val foodTruckService: FoodTruckService =  Retrofit.Builder()
        .baseUrl("https://api.foodtruck.schedgo.com")
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
        .create(FoodTruckService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        title = "Food Trucks"
        db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "app-db")
            .build()
        TruckDao = db.truckDao()
        ItemDao = db.itemDao()

    }


}