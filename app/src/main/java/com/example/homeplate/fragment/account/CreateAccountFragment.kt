package com.example.homeplate.fragment.account

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.homeplate.R
import com.example.homeplate.activity.account.OwnerSignupActivity
import com.example.homeplate.activity.account.UserSignupActivity
import kotlinx.android.synthetic.main.fragment_create_account.*

@SuppressLint("ValidFragment")
class CreateAccountFragment(): Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return layoutInflater.inflate(R.layout.fragment_create_account, container, false)
    }

    override fun onStart() {
        super.onStart()
        create_owner_account.setOnClickListener{
            val intent = Intent(context, OwnerSignupActivity::class.java)
            startActivity(intent)
        }
        create_user_account.setOnClickListener {
            val intent = Intent(context, UserSignupActivity::class.java)
            startActivity(intent)
        }
    }
}
