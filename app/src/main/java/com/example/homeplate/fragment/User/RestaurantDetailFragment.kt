package com.example.homeplate.fragment.User

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.homeplate.R
import com.example.homeplate.model.DishItem
import com.example.homeplate.model.RestaurantItem
import kotlinx.android.synthetic.main.fragment_menu.view.*
import kotlinx.android.synthetic.main.fragment_restaurant_list.view.*


@SuppressLint("ValidFragment")
class RestaurantDetailFragment(context: Context): Fragment(){

    private var listener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View =  inflater.inflate(R.layout.fragment_menu, container, false)


        view.GV2.adapter =  Adapter_RestaurantItem(this.requireActivity(), R.layout.fragment_menu_item, data)

        return view
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


    val data : ArrayList<DishItem>
        get()
        {
            val item_list: ArrayList<DishItem> = ArrayList<DishItem>()

            item_list.add(DishItem(R.drawable.dish1, "DISH 1"))
            item_list.add(DishItem(R.drawable.dish2, "DISH 2"))
            item_list.add(DishItem(R.drawable.dish3, "DISH 3"))
            item_list.add(DishItem(R.drawable.dish4, "DISH 4"))
            item_list.add(DishItem(R.drawable.dish5, "DISH 5"))
            item_list.add(DishItem(R.drawable.dish6, "DISH 6"))
            item_list.add(DishItem(R.drawable.dish1, "DISH 1"))
            item_list.add(DishItem(R.drawable.dish2, "DISH 2"))
            item_list.add(DishItem(R.drawable.dish3, "DISH 3"))
            item_list.add(DishItem(R.drawable.dish4, "DISH 4"))
            item_list.add(DishItem(R.drawable.dish5, "DISH 5"))
            item_list.add(DishItem(R.drawable.dish6, "DISH 6"))

            return item_list
        }





    inner class Adapter_RestaurantItem (private val getContext: Context, private val GridItemId : Int, private val Grid_item : ArrayList<DishItem>)
        : ArrayAdapter<DishItem>(getContext, GridItemId, Grid_item){

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
//        return super.getView(position, convertView, parent)

            var row = convertView
            val Holder : ViewHolder

            if (row == null){
                val inflater = (getContext as Activity).layoutInflater

                row = inflater!!.inflate(GridItemId, parent, false)

                Holder = ViewHolder()
                Holder.img = row!!.findViewById(R.id.img2) as ImageView
                Holder.txt = row!!.findViewById(R.id.txt2) as TextView

                row.setTag(Holder)
            }else{
                Holder = row.getTag() as ViewHolder

            }

            val item = Grid_item[position]
            Holder.img!!.setImageResource(item.image)
            Holder.txt!!.setText(item.name)



            return row



        }

        inner class ViewHolder{
            internal var img : ImageView? = null
            internal var txt : TextView? = null

        }
    }
}