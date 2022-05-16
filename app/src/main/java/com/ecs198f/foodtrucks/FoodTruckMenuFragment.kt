package com.ecs198f.foodtrucks

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.ecs198f.foodtrucks.databinding.FragmentFoodTruckDetailBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch


class FoodTruckMenuFragment(private var args: FoodTruckDetailFragmentArgs) : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_food_truck_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerViewAdapter = FoodItemListRecyclerViewAdapter(listOf())
        args.foodTruck.let {
            view.findViewById<RecyclerView>(R.id.foodItemListRecyclerView).apply {
                adapter = recyclerViewAdapter
                layoutManager = LinearLayoutManager(context)
            }
            (requireActivity() as MainActivity).apply {
                title = it.name

                var truckID = it.id
                foodTruckService.listFoodItems(it.id).enqueue(object : Callback<List<FoodItem>> {
                    override fun onResponse(
                        call: Call<List<FoodItem>>,
                        response: Response<List<FoodItem>>
                    ) {
                        recyclerViewAdapter.updateItems(response.body()!!)

                        var list : List<FoodItem>
                        lifecycleScope.launch {
                            ItemDao.delTruckItems(truckID)
                            ItemDao.addFoodItems(response.body()!!)
                            list = ItemDao.listAllItemsByTruck(truckID)
                            Log.i("db item list", list.toString())

                        }
                    }

                    override fun onFailure(call: Call<List<FoodItem>>, t: Throwable) {
                        throw t
                    }
                })
            }
        }

    }
}