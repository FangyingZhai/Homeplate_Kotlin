package com.example.homeplate.fragment.user

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.homeplate.R
import com.example.homeplate.activity.user.MenuActivity
import com.example.homeplate.activity.user.NavigationActivity
import com.example.homeplate.activity.user.PaymentActivity
import com.example.homeplate.adapter.MenuItemAdapter
import com.example.homeplate.model.DishItem
import com.example.homeplate.model.UserModel
import com.example.homeplate.utils.GlideApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.fragment_dashboard.*
import kotlinx.android.synthetic.main.fragment_order.*

@SuppressLint("ValidFragment")
class OrderFragment(context: Context, resEmail: String): Fragment() {
    private val parentContext = context

    private lateinit var auth : FirebaseAuth
    private lateinit var db : FirebaseFirestore
    private val resEmail = resEmail
    private lateinit var email : String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        email = auth.currentUser!!.email!!
        val view: View =  inflater.inflate(R.layout.fragment_order, container, false)
        return view
    }

    override fun onStart() {
        super.onStart()
        val adapter = OrderItemAdapter(MenuActivity.order.list)
        LV.adapter = adapter
        //start navigation
        navButton.setOnClickListener {
            Log.d("DEBUG", "NAVIGATION CLICKED")
            val intent = Intent(context, NavigationActivity::class.java)
            intent.putExtra("restaurant email", resEmail)
            startActivity(intent)
        }
        //check out order
        checkOutButton.setOnClickListener {
            if (MenuActivity.order.list.size > 0) {
                var user : UserModel
                db.collection("users").document(email).get().addOnCompleteListener {task ->
                    user = task.result!!.toObject(UserModel::class.java)!!
                    val newOrderNum :String = (user.orders.toInt()+1).toString()
                    var itemNum = 0
                    for (dish in MenuActivity.order.list) {
                        Log.e("DISH:", dish.name)
                        val dishInfo = HashMap<String, Any>()
                        dishInfo.put("name", dish.name)
                        dishInfo.put("price", dish.price)
                        db.collection("owners").document(resEmail).collection("orders")
                            .document(email).collection("order$newOrderNum")
                            .document(itemNum.toString()).set(dishInfo)
                        itemNum++
                    }
                    db.collection("users").document(email).update("orders", newOrderNum)

                    var total = 0.0
                    for (dish in MenuActivity.order.list) {
                        total += dish.price.toDouble()
                    }
                    price.text = "TOTAL PRICE: $total"
                    Toast.makeText(context, "Order placed!", Toast.LENGTH_SHORT).show()

                    val intent = Intent(context, PaymentActivity::class.java)
                    intent.putExtra("price", total.toString())
                    startActivity(intent)

                    MenuActivity.order.list.clear()
                    adapter.notifyDataSetChanged()
                }
                }

            else {
                Toast.makeText(context, "No items ordered.", Toast.LENGTH_SHORT).show()
            }
        }
        //refresh order list
        refreshButton.setOnClickListener {
            adapter.notifyDataSetChanged()
            var total = 0.0
            for (dish in MenuActivity.order.list) {
                total += dish.price.toDouble()
            }
            price.text = "TOTAL PRICE: $total"
        }
    }

    inner class OrderItemAdapter(orderList: ArrayList<DishItem>) : BaseAdapter() {

        val orderList = orderList

        inner class ViewHolder(row: View?) {
            var name: TextView? = null
            var price: TextView? = null

            init {
                this.name = row?.findViewById(R.id.txt)
                this.price = row?.findViewById(R.id.txt2)
            }
        }
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val view: View?
            val viewHolder: ViewHolder
            if (convertView == null) {
                val inflater = activity?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                view = inflater.inflate(R.layout.model_order_item, null)
                viewHolder = ViewHolder(view)
                view?.tag = viewHolder
            } else {
                view = convertView
                viewHolder = view.tag as ViewHolder
            }

            val dish = orderList[position]
            viewHolder.name?.text = dish.name
            viewHolder.price?.text = dish.price

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