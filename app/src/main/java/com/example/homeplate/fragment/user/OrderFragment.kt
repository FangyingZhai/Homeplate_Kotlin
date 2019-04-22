package com.example.homeplate.fragment.user

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.homeplate.R
import com.example.homeplate.activity.user.NavigationActivity
import com.example.homeplate.model.DishItem
import kotlinx.android.synthetic.main.fragment_order.*

@SuppressLint("ValidFragment")
class OrderFragment(context: Context, resEmail: String): Fragment() {
    private val parentContext = context
    private val resEmail = resEmail
    val orderList = ArrayList<DishItem>()

    private var listener: RestaurantListFragment.OnFragmentInteractionListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View =  inflater.inflate(R.layout.fragment_order, container, false)
        return view
    }

    override fun onStart() {
        super.onStart()
        navButton.setOnClickListener {
            Log.d("DEBUG", "NAVIGATION CLICKED")
            val intent = Intent(context, NavigationActivity::class.java)
            intent.putExtra("restaurant email", resEmail)
            startActivity(intent)
        }
    }

}