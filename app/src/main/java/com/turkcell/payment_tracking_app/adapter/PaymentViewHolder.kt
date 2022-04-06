package com.turkcell.payment_tracking_app.adapter

import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.turkcell.payment_tracking_app.R
import com.turkcell.payment_tracking_app.model.Payment

class PaymentViewHolder (itemView: View, var itemClick : (position:Int)->Unit) : RecyclerView.ViewHolder (itemView) {

    var tvPaymentPrice : TextView
    var tvPaymentDate : TextView

    init {
        tvPaymentPrice = itemView.findViewById(R.id.tvPaymentPrice)
        tvPaymentDate = itemView.findViewById(R.id.tvPaymentDate)

        itemView.setOnClickListener {
            itemClick(adapterPosition)
        }
    }

    fun bindData(payment: Payment){
        tvPaymentDate.text = payment.date
        tvPaymentPrice.text = payment.price.toString()
    }
}