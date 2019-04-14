package com.example.homeplate.fragment.owner

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.homeplate.R
import kotlinx.android.synthetic.main.fragment_edit.*
import java.lang.Exception


@SuppressLint("ValidFragment")
class EditFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        submit_info.setOnClickListener {
            try {
                val name = restaurant_name.text.toString().replace("\\d".toRegex(), "").trim()
                val value = telephone.text.toString()
                val email = email.text.toString()
                val address = address.text.toString()

                if(name.isEmpty() || value.isEmpty() || email.isEmpty() || address.isEmpty()) {
                    Toast.makeText(it.context, "Please enter valid input", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                if(name.toIntOrNull() != null) {
                    Toast.makeText(it.context, "Please enter valid name", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                //send data back to main activity.
//                setResult(
//                    Activity.RESULT_OK, Intent()
//                        .putExtra("name", name)
//                        .putExtra("value", value.toInt()))
//
//                finish()
            }
            catch (e : Exception) {
                Toast.makeText(it.context, "Please enter valid input", Toast.LENGTH_SHORT).show()
            }
        }
    }

//    // TODO: Rename method, update argument and hook method into UI event
//    fun onButtonPressed(uri: Uri) {
//        listener?.onFragmentInteraction(uri)
//    }

//    override fun onAttach(context: Context) {
//        super.onAttach(context)
//        if (context is OnFragmentInteractionListener) {
//            listener = context
//        } else {
//            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
//        }
//    }


    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(restaurant_name: Editable, telephone:Editable, email:Editable, address: Editable)
    }
}
