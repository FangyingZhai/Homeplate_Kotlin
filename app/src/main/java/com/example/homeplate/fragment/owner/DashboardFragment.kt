package com.example.homeplate.fragment.owner

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.homeplate.R
import com.example.homeplate.activity.owner.AddFoodActivity
import com.example.homeplate.adapter.MenuItemAdapter
import com.example.homeplate.model.DishItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_dashboard.*
import kotlinx.android.synthetic.main.fragment_info.*


@SuppressLint("ValidFragment")
class DashboardFragment(context: Context) : Fragment() {
    private var parentContext = context
    private lateinit var auth : FirebaseAuth
    private lateinit var db : FirebaseFirestore
    private lateinit var email : String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        email = auth.currentUser!!.email!!
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun onStart() {
        super.onStart()
        addFoodButton.setOnClickListener {
            val intent = Intent(parentContext, AddFoodActivity::class.java)
            startActivity(intent)
        }
        getMenu()
    }

    private fun getMenu() {
        val menuList = ArrayList<DishItem>()
        db.collection("owners").document(email).collection("menu")
            .get().addOnCompleteListener{ task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        val dish = document.toObject(DishItem::class.java)
                        menuList.add(dish)
                        Log.d("DEBUG", "menu list: "+dish.name)
                    }
                }
                else {
                    Log.d("DEBUG", "COULDN'T GET MENU LIST")
                }
                GV.adapter =  MenuItemAdapter(parentContext, menuList, email, "owner")
            }
    }
}
