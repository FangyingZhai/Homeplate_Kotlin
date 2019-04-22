package com.example.homeplate.fragment.user

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.homeplate.R
import com.example.homeplate.adapter.RestaurantItemAdapter
import com.example.homeplate.model.RestaurantItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_restaurant_list.*

@SuppressLint("ValidFragment")
class RestaurantListFragment(context: Context): Fragment() {
    private var parentContext = context
    private lateinit var auth : FirebaseAuth
    private lateinit var db : FirebaseFirestore
    private lateinit var email : String

    private var listener: OnFragmentInteractionListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        email = auth.currentUser!!.email!!
        return inflater.inflate(R.layout.fragment_restaurant_list, container, false)
    }

    override fun onStart() {
        super.onStart()
        getRestaurantList()
    }


    //  用来refresh UI， 当database改变的时候
    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            fragmentManager!!.beginTransaction().detach(this).attach(this).commit()
        }
    }



    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    private fun getRestaurantList() {
        var restaurantList = ArrayList<RestaurantItem>()
        db.collection("owners")
            .get().addOnCompleteListener{ task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        val restaurant = document.toObject(RestaurantItem::class.java)
                        restaurantList.add(restaurant)
                        Log.d("DEBUG", "restaurant list: "+restaurant.name)
                    }
                }
                else {
                    Log.d("DEBUG", "COULDN'T GET RESTAURANT LIST")
                }
                GV3.adapter =  RestaurantItemAdapter(context!!, restaurantList)
            }
    }

}

