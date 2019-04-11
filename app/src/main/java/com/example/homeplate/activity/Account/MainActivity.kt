package com.example.homeplate.activity.Account

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.homeplate.R
import com.example.homeplate.adapter.AccountPagerAdapter
import com.google.firebase.FirebaseApp
import kotlinx.android.synthetic.main.activity_account.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)

        val adapter = AccountPagerAdapter(this, supportFragmentManager)

        viewpager.adapter = adapter
        tabs.setupWithViewPager(viewpager)
    }
}