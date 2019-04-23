package com.example.homeplate.fragment.user

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.homeplate.R
import com.example.homeplate.activity.account.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_user_settings.*

@SuppressLint("ValidFragment")
class UserSettingsFragment: Fragment() {

    private lateinit var auth : FirebaseAuth
    private lateinit var db : FirebaseFirestore
    private lateinit var email : String
    private var listener: RestaurantListFragment.OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        email = auth.currentUser!!.email!!
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View =  inflater.inflate(R.layout.fragment_user_settings, container, false)
        return view
    }

    override fun onStart() {
        super.onStart()
        db.collection("users").document(email).get().addOnCompleteListener { document ->
            name.text = document.result!!["firstName"].toString() + " " +document.result!!["lastName"].toString()
            email2.text = "Email Address: "+document.result!!.id
        }
        signOutButton.setOnClickListener {
            Log.d("DEBUG", "SIGN-OUT CLICKED")
            auth.signOut()
            Toast.makeText(context, "Sign-out successful.", Toast.LENGTH_SHORT).show()
            val intent = Intent(context, MainActivity::class.java)
            startActivity(intent)
            activity!!.finish()
        }
    }

}