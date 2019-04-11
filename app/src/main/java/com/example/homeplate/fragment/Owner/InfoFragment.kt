package com.example.homeplate.fragment.Owner

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.content.ContextCompat.getSystemService
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.homeplate.R
import kotlinx.android.synthetic.main.fragment_info.*

@SuppressLint("ValidFragment")
class InfoFragment(context: Context) : Fragment() , EditFragment.OnFragmentInteractionListener {
    private var parentContext = context

    override fun onStart() {
        super.onStart()

        edit.setOnClickListener {
            val fm = activity!!.supportFragmentManager
            fm.beginTransaction().replace(R.id.frag_placeholder, EditFragment()).addToBackStack("null").commit()
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_info, container, false)
    }

    override fun onFragmentInteraction(restaurant_name: Editable, telephone:Editable, email:Editable, address: Editable) {
        resname.text = restaurant_name;
        tele.text = telephone;
        email_address.text = email;
        res_address.text = address;
    }



}
