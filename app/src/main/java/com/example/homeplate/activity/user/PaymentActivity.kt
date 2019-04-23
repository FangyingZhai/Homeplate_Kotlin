package com.example.homeplate.activity.user

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.craftman.cardform.Card
import com.craftman.cardform.CardForm
import com.craftman.cardform.OnPayBtnClickListner
import com.example.homeplate.R

class PaymentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        val cardForm = findViewById(R.id.cardform) as CardForm
        val txtDes = findViewById(R.id.payment_amount) as TextView
        val btnPay = findViewById(R.id.btn_pay) as Button

        txtDes.text = intent.getStringExtra("price")
        btnPay.text = String.format("Payer %s", txtDes.text)


        cardForm.setPayBtnClickListner(object : OnPayBtnClickListner {
            override fun onClick(card: Card) {
                Toast.makeText(
                    this@PaymentActivity,
                    "Name : " + card.getName() + " | Last 4 digits :" + card.getLast4(),
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}
