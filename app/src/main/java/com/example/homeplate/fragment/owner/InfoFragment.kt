package com.example.homeplate.fragment.owner

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.homeplate.R
import com.example.homeplate.activity.account.MainActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_info.*

@SuppressLint("ValidFragment")
class InfoFragment(context: Context) : Fragment() , EditFragment.OnFragmentInteractionListener {
    private var parentContext = context
    private lateinit var auth : FirebaseAuth

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        auth = FirebaseAuth.getInstance()
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_info, container, false)
    }

    override fun onStart() {
        super.onStart()

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
    }

    override fun onFragmentInteraction(restaurant_name: Editable, telephone:Editable, email:Editable, address: Editable) {
        resname.text = restaurant_name;
        tele.text = telephone;
        email_address.text = email;
        res_address.text = address;
    }

}
