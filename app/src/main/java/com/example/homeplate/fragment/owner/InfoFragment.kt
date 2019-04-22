package com.example.homeplate.fragment.owner

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.homeplate.R
import com.example.homeplate.activity.account.MainActivity
import com.example.homeplate.activity.owner.AddFoodActivity
import com.example.homeplate.activity.user.MenuActivity
import com.example.homeplate.adapter.MenuItemAdapter
import com.example.homeplate.model.DishItem
import com.example.homeplate.model.RestaurantItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_edit.*
import kotlinx.android.synthetic.main.fragment_info.*

@SuppressLint("ValidFragment")
class InfoFragment(context: Context) : Fragment() , EditFragment.OnFragmentInteractionListener {
    private var parentContext = context
    private lateinit var auth : FirebaseAuth
    private lateinit var db : FirebaseFirestore
    private lateinit var email : String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        email = auth.currentUser!!.email!!
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_info, container, false)
    }

    override fun onStart() {
        super.onStart()
        //buttons
        edit.setOnClickListener {
            val fm = activity!!.supportFragmentManager
            fm.beginTransaction().replace(R.id.frag_placeholder, EditFragment()).addToBackStack("null").commit()
        }
        signOutButton.setOnClickListener {
            Log.d("DEBUG", "SIGN-OUT CLICKED")
            auth.signOut()
            Toast.makeText(parentContext, "Sign-out successful.", Toast.LENGTH_SHORT).show()
            val intent = Intent(parentContext, MainActivity::class.java)
            startActivity(intent)
            activity!!.finish()
        }
        //restaurant info
        db.collection("owners").document(email).get().addOnCompleteListener { task ->
            val myRestaurant = task.result!!.toObject(RestaurantItem::class.java)!!
            resname.text = "Name: " + myRestaurant.name
            tele.text = "Phone: "+myRestaurant.phone
            email_address.text = "Email address: "+myRestaurant.email
            res_address.text = "Address: "+myRestaurant.address
        }

    }

    override fun onFragmentInteraction(restaurant_name: Editable, telephone:Editable, email:Editable, address: Editable) {
        resname.text = restaurant_name;
        tele.text = telephone;
        email_address.text = email;
        res_address.text = address;
    }
}
