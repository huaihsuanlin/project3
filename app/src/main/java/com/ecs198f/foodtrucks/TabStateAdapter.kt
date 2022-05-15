package com.ecs198f.foodtrucks

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.navArgument
import androidx.viewpager2.adapter.FragmentStateAdapter


class TabStateAdapter(fragment: Fragment, private var args: FoodTruckDetailFragmentArgs) :
    FragmentStateAdapter(fragment){

    override fun getItemCount(): Int = 2
    override fun createFragment(position: Int): Fragment {
        when(position){
            0 -> return FoodTruckMenuFragment(args)
        }
        return FoodTruckReviewsFragment(args)
    }
}

