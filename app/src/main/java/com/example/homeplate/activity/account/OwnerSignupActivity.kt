package com.example.homeplate.activity.account

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.homeplate.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_owner_signup.*

class OwnerSignupActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_owner_signup)
        auth = FirebaseAuth.getInstance()
    }

    override fun onStart() {
        super.onStart()
        create_account.setOnClickListener {
            val email = email2.text.toString()
            val password = password.text.toString()
            val restaurant = restaurant.text.toString()
            val address = address.text.toString()
            val phone = phone.text.toString()

            if (email != "" && password != "" && restaurant != "") {

                this.auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // add account info to firestore
                            val db = FirebaseFirestore.getInstance()
                            var user = HashMap<String, Any>()
                            user.put("email", email)
                            user.put("name", restaurant)
                            user.put("address", address)
                            user.put("phone", phone)
                            db.collection("owners").document(email).set(user)
                            // Account creation success
                            Toast.makeText(this, "Account made.", Toast.LENGTH_SHORT).show()
                            // End activity
                            finish()
                        } else {
                            // If account creation fails, display a message to the user.
                            Toast.makeText(this, " Valid email and password of length >= 6 required.",
                                Toast.LENGTH_SHORT).show()
                        }
                    }

            }

            else {
                Toast.makeText(this, "Must fill all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
