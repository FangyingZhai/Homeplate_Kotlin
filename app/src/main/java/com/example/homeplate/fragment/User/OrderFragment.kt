package com.example.homeplate.fragment.User

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.homeplate.R
import com.example.homeplate.fragment.User.RestaurantListFragment

@SuppressLint("ValidFragment")
class OrderFragment(): Fragment() {


    private var listener: RestaurantListFragment.OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View =  inflater.inflate(R.layout.fragment_order, container, false)


        return view
    }

}