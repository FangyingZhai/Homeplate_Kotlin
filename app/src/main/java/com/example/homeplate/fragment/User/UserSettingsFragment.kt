package com.example.homeplate.fragment.User

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
import com.example.homeplate.activity.Account.MainActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_info.*

@SuppressLint("ValidFragment")
class UserSettingsFragment: Fragment() {

    private lateinit var auth : FirebaseAuth
    private var listener: RestaurantListFragment.OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View =  inflater.inflate(R.layout.fragment_user_settings, container, false)
        return view
    }

    override fun onStart() {
        super.onStart()

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