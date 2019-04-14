package com.example.homeplate.activity.user

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.example.homeplate.R
import com.example.homeplate.fragment.user.RestaurantListFragment
import com.example.homeplate.fragment.user.UserSettingsFragment
import kotlinx.android.synthetic.main.activity_user_homepage.*

class UserHomepageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_homepage)

        val fragmentAdapter = MyPagerAdapter(supportFragmentManager)
        viewpager_main.adapter = fragmentAdapter
        tabs_main.setupWithViewPager(viewpager_main)
    }


    inner class MyPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        //        不同的position对应不同的tab
        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> {
                    RestaurantListFragment(this@UserHomepageActivity)
                }
                else -> UserSettingsFragment()
            }
        }

        //      总共有多少tab
        override fun getCount(): Int {
            return 2
        }

        //      每个page的名字
        override fun getPageTitle(position: Int): CharSequence {
            return when (position) {
                0 -> "Restaurants"
                else -> {
                    return "Settings"
                }
            }
        }
    }
}
