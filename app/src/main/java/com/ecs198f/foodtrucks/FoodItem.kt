package com.ecs198f.foodtrucks

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize


@Parcelize
@Entity
data class FoodItem(
    val id: String,
    val truckId: String,
    val name: String,
    val description: String,
    val price: Double,
    val taxIncluded: Boolean
): Parcelable {
    @PrimaryKey(autoGenerate = true) var key: Int = 0

}
