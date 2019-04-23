package com.example.homeplate.adapter

import android.app.Activity
import android.content.Context
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.homeplate.R
import com.example.homeplate.model.DishItem
import com.example.homeplate.utils.GlideApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

private lateinit var auth : FirebaseAuth
private lateinit var db : FirebaseFirestore
private lateinit var photoRef: StorageReference
private lateinit var email: String
private lateinit var Holder: MenuItemAdapter.ViewHolder

class MenuItemAdapter (context: Context, dish_List: ArrayList<DishItem>,
                       resEmail: String, account: String): BaseAdapter(){

    val context = context
    val dish_List = dish_List
    val resEmail = resEmail
    val account = account


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
            //owner account, delete menu items
            if (account == "owner") {
                Log.d("DEBUG", "CLICKED ${item.name}")
                val alertDialog = AlertDialog.Builder(context)
                alertDialog.setTitle("Confirm")
                alertDialog.setMessage("Are you sure you want to delete ${item.name}?")
                alertDialog.setPositiveButton("OK") { _, _ ->
                    val storageRef = FirebaseStorage.getInstance().reference
                    storageRef.child("/photos/"+resEmail+"/"+item.name).delete()
                    val db = FirebaseFirestore.getInstance()
                    db.collection("owners").document(resEmail).collection("menu")
                        .document(item.name).delete()
                    Toast.makeText(context, "${item.name} deleted.",
                        Toast.LENGTH_SHORT).show()
                }
                alertDialog.setNegativeButton("CANCEL") { _, _ -> }
                alertDialog.show()
            }
            //user account
            else {
                val alertDialog = AlertDialog.Builder(context)
                alertDialog.setTitle("Confirm")
                alertDialog.setMessage("Add ${item.name} to order?")
                alertDialog.setPositiveButton("OK") { _, _ ->
                    val orderRef = db.collection("owners").document(resEmail).collection("orders").document(email).collection("orders")
                    Toast.makeText(context, "${item.name} added to order.", Toast.LENGTH_SHORT).show()
                }
                alertDialog.setNegativeButton("CANCEL") { _, _ -> }
                alertDialog.show()
            }

        }
        return row!!
    }


    private fun getImage(item: DishItem) {
        val storageRef = FirebaseStorage.getInstance().reference
        photoRef = storageRef.child("/photos/"+resEmail+"/"+item.name)
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
