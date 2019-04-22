package com.example.homeplate.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.homeplate.R
import com.example.homeplate.activity.user.MenuActivity
import com.example.homeplate.model.DishItem
import com.example.homeplate.model.RestaurantItem
import com.example.homeplate.utils.GlideApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

private lateinit var photoRef: StorageReference
private lateinit var email: String
private lateinit var Holder: RestaurantItemAdapter.ViewHolder

class RestaurantItemAdapter (context: Context, restaurant_List: ArrayList<RestaurantItem>): BaseAdapter(){

    val context = context
    val restaurant_List = restaurant_List

    override fun getCount(): Int {
        return restaurant_List.size
    }
    override fun getItem(position: Int): RestaurantItem? {
        return restaurant_List[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var row = convertView
        if (row == null) {
            val inflater = (context as Activity).layoutInflater

            row = inflater.inflate(R.layout.model_restaurant_item, parent, false)

            Holder = ViewHolder()
            Holder.img = row.findViewById(R.id.img) as ImageView
            Holder.txt = row.findViewById(R.id.txt) as TextView

            row.setTag(Holder)
        } else {
            Holder = row.getTag() as ViewHolder
        }

        val item = restaurant_List[position]
        getImage(item)
        Holder.txt!!.setText(item.name)

        row!!.setOnClickListener {
            val intent = Intent(context, MenuActivity()::class.java)
            intent.putExtra("RESTAURANT_EMAIL", item.email)
            context.startActivity(intent)
        }
        return row
    }


    private fun getImage(item: RestaurantItem) {
        val storageRef = FirebaseStorage.getInstance().reference
        photoRef = storageRef.child("/photos/restaurant_profiles/"+item.email+".jpg")
        GlideApp.with(context).load(photoRef).into(Holder.img!!)
    }

    inner class ViewHolder{
        var img : ImageView? = null
        var txt : TextView? = null
    }
}
