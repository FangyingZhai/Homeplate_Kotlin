package com.example.homeplate.activity.account

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.homeplate.R
import com.example.homeplate.activity.owner.OwnerHomepageActivity
import com.example.homeplate.activity.user.UserHomepageActivity
import com.example.homeplate.adapter.AccountPagerAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_account.*

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        // Check if user is signed in (non-null) and sign-in accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val email = auth.currentUser!!.email!!
            // Check if account is user or owner
            val db = FirebaseFirestore.getInstance()
            // Checks if email is in "users" collection
            db.collection("users").document(email).get().addOnSuccessListener { document ->
                // Email exists in user collection, so it is a user account
                if (document.exists()) {
                    //Log.d("DEBUG", "DOCUMENT DATA: ${document.data}")
                    Toast.makeText(this, "User account sign-in successful.", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, UserHomepageActivity::class.java)
                    intent.putExtra("email", email)
                    startActivity(intent)
                    //finish account activity so that back button does not go back to account stuff
                    finish()
                }

                // Email doesn't exist in user collection, but sign-in was successful so it is an owner account
                else {
                    Toast.makeText(this, "Owner account sign-in successful.", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, OwnerHomepageActivity::class.java)
                    intent.putExtra("email", email)
                    startActivity(intent)
                    //finish account activity so that back button does not go back to account stuff
                    finish()
                }
            }
        }
        //No account logged in, loads account sign-in/sign-up UI
        else {
            setContentView(R.layout.activity_account)
            val adapter = AccountPagerAdapter(this, supportFragmentManager)
            viewpager.adapter = adapter
            tabs.setupWithViewPager(viewpager)
        }
    }
}