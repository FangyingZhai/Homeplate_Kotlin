package com.example.homeplate.activity.owner

import android.app.Notification
import android.content.Context
import android.net.ConnectivityManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.util.Log
import com.example.homeplate.R
import com.example.homeplate.fragment.owner.DashboardFragment
import com.example.homeplate.fragment.owner.InfoFragment
import com.example.homeplate.fragment.owner.NoConnectionFragment
import com.example.homeplate.fragment.owner.NotificationsFragment
import kotlinx.android.synthetic.main.activity_owner_homepage.*

class OwnerHomepageActivity : AppCompatActivity() {

    private var isNetworkConnected = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_owner_homepage)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        val fm = supportFragmentManager
        val ft = fm.beginTransaction()
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        if (networkInfo == null) {
            Log.e("NETWORK", "not connected")
            ft.add(R.id.frag_placeholder, NoConnectionFragment())
        }
        else {
            Log.e("NETWORK", "connected")
            ft.add(R.id.frag_placeholder, InfoFragment(this@OwnerHomepageActivity))
            this.isNetworkConnected = true
        }
        ft.commit()
    }


    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                if (this.isNetworkConnected) {
                    val fm = supportFragmentManager
                    val ft = fm.beginTransaction()
                    ft.replace(R.id.frag_placeholder, InfoFragment(this@OwnerHomepageActivity))
                    ft.commit()
                }
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                if (this.isNetworkConnected) {
                    val fm = supportFragmentManager
                    val ft = fm.beginTransaction()
                    ft.replace(R.id.frag_placeholder, DashboardFragment(this@OwnerHomepageActivity))
                    ft.commit()
                }
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                if (this.isNetworkConnected) {
                    val fm = supportFragmentManager
                    val ft = fm.beginTransaction()
                    ft.replace(R.id.frag_placeholder, NotificationsFragment(this@OwnerHomepageActivity))
                    ft.commit()
                }
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

}
