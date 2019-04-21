package com.example.homeplate.activity.user

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v7.app.AppCompatActivity
import com.example.homeplate.R
import com.example.homeplate.fragment.user.OrderFragment
import com.example.homeplate.fragment.user.RestaurantDetailFragment
import kotlinx.android.synthetic.main.activity_menu.*

class MenuActivity : AppCompatActivity() {
    private lateinit var restaurantEmail: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //intent(passed restaurant email)
        restaurantEmail = intent.extras.getString("RESTAURANT_EMAIL")!!
        //ui
        setContentView(R.layout.activity_menu)
        val fragmentAdapter = MyPagerAdapter(supportFragmentManager)
        viewpager_main2.adapter = fragmentAdapter
        tabs_main2.setupWithViewPager(viewpager_main2)
    }


    inner class MyPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        //        不同的position对应不同的tab
        override fun getItem(position: Int):Fragment  {
            return when (position) {
                0 -> {
                    RestaurantDetailFragment( this@MenuActivity, restaurantEmail)
                }
                else -> OrderFragment()
            }
        }

        //      总共有多少tab
        override fun getCount(): Int {
            return 2
        }

        //      每个page的名字
        override fun getPageTitle(position: Int): CharSequence {
            return when (position) {
                0 -> "Dishes"
                else -> {
                    return "Orders"
                }
            }
        }
    }
}
