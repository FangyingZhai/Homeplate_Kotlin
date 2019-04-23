package com.example.homeplate.fragment.account

import android.annotation.SuppressLint
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.homeplate.R
import com.example.homeplate.activity.owner.OwnerHomepageActivity
import com.example.homeplate.activity.user.UserHomepageActivity
import kotlinx.android.synthetic.main.fragment_sign_in.*

@SuppressLint("ValidFragment")
class SignInFragment(): Fragment() {

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        auth = FirebaseAuth.getInstance()
        return layoutInflater.inflate(R.layout.fragment_sign_in, container, false)
    }

    override fun onStart() {
        super.onStart()

        sign_in.setOnClickListener {
            val email = email2.text.toString()
            val password = password.text.toString()

            if (email != "" && password != "") {
                this.auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Check if account is user or owner
                            val db = FirebaseFirestore.getInstance()
                            // Checks if email is in "users" collection
                            db.collection("users").document(email).get().addOnSuccessListener { document ->

                                // Email exists in user collection, so it is a user account
                                if (document.exists()) {
                                    //Log.d("DEBUG", "DOCUMENT DATA: ${document.data}")
                                    Toast.makeText(context, "User account sign-in successful.", Toast.LENGTH_SHORT).show()
                                    val intent = Intent(context, UserHomepageActivity::class.java)
                                    intent.putExtra("email", email)
                                    startActivity(intent)
                                    //finish account activity so that back button does not go back to account stuff
                                    activity!!.finish()
                                }

                                // Email doesn't exist in user collection, but sign-in was successful so it is an owner account
                                else {
                                    Toast.makeText(context, "Owner account sign-in successful.", Toast.LENGTH_SHORT).show()
                                    val intent = Intent(context, OwnerHomepageActivity::class.java)
                                    intent.putExtra("email", email)
                                    startActivity(intent)
                                    //finish account activity so that back button does not go back to account stuff
                                    activity!!.finish()
                                }
                            }

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(context, "Authentication failed.", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
            else {
                Toast.makeText(context, "Must fill all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }
}