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
import com.example.homeplate.adapter.MenuItemAdapter
import com.example.homeplate.model.DishItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_menu.*


@SuppressLint("ValidFragment")
class RestaurantDetailFragment(context: Context, resEmail: String): Fragment(){
    private lateinit var auth : FirebaseAuth
    private lateinit var db : FirebaseFirestore
    private lateinit var email : String
    private val restaurantEmail  = resEmail

    private var listener: OnFragmentInteractionListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //firebase stuff
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        email = auth.currentUser!!.email!!
        //restaurantEmail = intent
        val view: View =  inflater.inflate(R.layout.fragment_menu, container, false)
        //view.GV2.adapter =  Adapter_RestaurantItem(this.requireActivity(), R.layout.fragment_menu_item, data)
        return view
    }

    override fun onStart() {
        super.onStart()
        getMenu()
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

    private fun getMenu() {
        var menuList = ArrayList<DishItem>()
        db.collection("owners").document(restaurantEmail).collection("menu")
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
                GV2.adapter =  MenuItemAdapter(context!!, menuList, restaurantEmail, "user")
            }
    }
}