package com.ecs198f.foodtrucks

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.motion.widget.Debug
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.ecs198f.foodtrucks.databinding.FragmentFoodTruckListBinding
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FoodTruckListFragment : Fragment() {

    private fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager != null) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                    return true
                }
            }
        }
        return false
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentFoodTruckListBinding.inflate(inflater, container, false)
        val recyclerViewAdapter = FoodTruckListRecyclerViewAdapter(listOf())

        binding.foodTruckListRecyclerView.apply {
            adapter = recyclerViewAdapter
            layoutManager = LinearLayoutManager(context)
        }

        (requireActivity() as MainActivity).apply {
            title = "Food Trucks"

            if(!isOnline(this)) {
                lifecycleScope.launch {
                    recyclerViewAdapter.updateItems(TruckDao.listAllTrucks())
                    Log.i("no internet", "database used to generate recyclerview")
                }
            }
            else {
                foodTruckService.listFoodTrucks().enqueue(object : Callback<List<FoodTruck>> {
                    override fun onResponse(
                        call: Call<List<FoodTruck>>,
                        response: Response<List<FoodTruck>>
                    ) {
                        recyclerViewAdapter.updateItems(response.body()!!)
                        Log.i("response", response.body().toString()!!)

                        var list : List<FoodTruck>
                        lifecycleScope.launch {
                            TruckDao.deleteTrucks()
                            TruckDao.addTrucks(response.body()!!)
                            list = TruckDao.listAllTrucks()
                            Log.i("db truck list", list.toString())

                        }

                    }

                    override fun onFailure(call: Call<List<FoodTruck>>, t: Throwable) {
                        throw t
                    }
                })
            }

        }

        return binding.root
    }
}
