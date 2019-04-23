package com.example.homeplate.fragment.user

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.homeplate.R
import com.example.homeplate.activity.user.MenuActivity
import com.example.homeplate.adapter.*
import com.example.homeplate.model.DishItem
import com.example.homeplate.utils.GlideApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.fragment_menu.*


@SuppressLint("ValidFragment")
class RestaurantDetailFragment(context: Context, resEmail: String): Fragment(){
    private var parentContext = context
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
        //view.GV2.adapter =  Adapter_RestaurantItem(this.requireActivity(), R.layout.model_dish_item, data)
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
                GV2.adapter =  UserMenuItemAdapter(context!!, menuList, restaurantEmail, "user")
            }
    }

    inner class UserMenuItemAdapter (context: Context, dish_List: ArrayList<DishItem>,
                           resEmail: String, account: String): BaseAdapter(){

        private val context = context
        private val dish_List = dish_List
        private val resEmail = resEmail
        private val account = account
        private lateinit var photoRef: StorageReference
        private lateinit var Holder: UserMenuItemAdapter.ViewHolder

        override fun getCount(): Int {
            return dish_List.size
        }
        override fun getItem(position: Int): DishItem? {
            return dish_List[position]
        }

        override fun getItemId(position: Int): Long {
            return 0
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            //firebase stuff
            auth = FirebaseAuth.getInstance()
            db = FirebaseFirestore.getInstance()
            email = auth.currentUser!!.email!!
            //ui
            var row = convertView
            if (row == null) {
                val inflater = (context as Activity).layoutInflater

                row = inflater.inflate(R.layout.model_dish_item, parent, false)

                Holder = ViewHolder()
                Holder.img = row.findViewById(R.id.img) as ImageView
                Holder.txt = row.findViewById(R.id.txt) as TextView
                Holder.txt2 = row.findViewById(R.id.txt2) as TextView

                row.setTag(Holder)
            } else {
                Holder = row.getTag() as ViewHolder
            }

            val item = dish_List[position]
            getImage(item)
            Holder.txt!!.setText(item.name)
            Holder.txt2!!.setText("Price: $"+item.price)
            Holder.img!!.setOnClickListener {
                val alertDialog = AlertDialog.Builder(context)
                alertDialog.setTitle("Confirm")
                alertDialog.setMessage("Add ${item.name} to order?")
                alertDialog.setPositiveButton("OK") { _, _ ->
                    MenuActivity.order.list.add(item)
                    Toast.makeText(context, "${item.name} added to order.", Toast.LENGTH_SHORT).show()
                }
                alertDialog.setNegativeButton("CANCEL") { _, _ -> }
                alertDialog.show()
            }
            return row!!
        }

        private fun getImage(item: DishItem) {
            val storageRef = FirebaseStorage.getInstance().reference
            photoRef = storageRef.child("/photos/"+ resEmail +"/"+item.name)
            GlideApp.with(context).load(photoRef).into(Holder.img!!)
        }

        fun refresh() {
            notifyDataSetChanged()
        }

        inner class ViewHolder{
            var img : ImageView? = null
            var txt : TextView? = null
            var txt2 : TextView? = null
        }
    }

}