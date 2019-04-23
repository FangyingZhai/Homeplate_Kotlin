package com.example.homeplate.fragment.owner

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

import com.example.homeplate.R
import com.example.homeplate.model.DishItem
import com.example.homeplate.model.OrderModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.storage.UploadTask

@SuppressLint("ValidFragment")
class NotificationsFragment(context: Context) : Fragment() {

    private var orderList = ArrayList<OrderModel>()
    private var userList = ArrayList<QueryDocumentSnapshot>()
    private lateinit var auth : FirebaseAuth
    private lateinit var db : FirebaseFirestore
    private lateinit var email : String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        email = auth.currentUser!!.email!!
        val view: View =  inflater.inflate(R.layout.fragment_notifications, container, false)
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        //users who ordered

    }

    private fun getUsers() {
        val userRef = db.collection("owners").document(email).collection("orders").get()
            .addOnCompleteListener {task ->
                for(user in task.result!!){
                    userList.add(user)
                }
                for (user in userList) {
                    //getOrders(user)
                }
            }
    }

//    private fun getOrders(user: QueryDocumentSnapshot) {
//        val orderRef = db.collection("owners").document(email).collection("orders")
//            .document(user.id).collection()
//        for (order in)
//    }


    inner class OrderItemAdapter(orderList: ArrayList<OrderModel>) : BaseAdapter() {

        val orderList = orderList

        inner class ViewHolder(row: View?) {
            var email: TextView? = null
            var orderList: TextView? = null
            var price: TextView? = null

            init {
                this.email = row?.findViewById(R.id.txt3)
                this.orderList = row?.findViewById(R.id.txt)
                this.price = row?.findViewById(R.id.txt2)
            }
        }
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val view: View?
            val viewHolder: ViewHolder
            if (convertView == null) {
                val inflater = activity?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                view = inflater.inflate(R.layout.model_owner_order_item, null)
                viewHolder = ViewHolder(view)
                view?.tag = viewHolder
            } else {
                view = convertView
                viewHolder = view.tag as ViewHolder
            }

            val order = orderList[position]
            //viewHolder.name?.text = dish.name
            //viewHolder.price?.text = dish.price

            return view as View        }

        override fun getItem(position: Int): Any {
            return orderList[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return orderList.size
        }
    }


}
