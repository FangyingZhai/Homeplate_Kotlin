package com.example.homeplate.activity.account

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.homeplate.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_user_signup.*

class UserSignupActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_signup)
        auth = FirebaseAuth.getInstance()
    }

    override fun onStart() {
        super.onStart()
        create_account.setOnClickListener {
            val email = email2.text.toString()
            val password = password.text.toString()
            val first_name = first_name.text.toString()
            val last_name = last_name.text.toString()

            if (email != "" && password != "" && first_name != "" && last_name != "") {

                this.auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // add account info to firestore
                            val db = FirebaseFirestore.getInstance()
                            var user = HashMap<String, Any>()
                            user.put("firstName", first_name)
                            user.put("lastName", last_name)
                            user.put("orders", "0")
                            db.collection("users").document(email).set(user)
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
