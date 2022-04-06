package com.turkcell.payment_tracking_app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.turkcell.payment_tracking_app.R
import com.turkcell.payment_tracking_app.model.Payment

class PaymentAdapter (var context: Context, var liste: ArrayList<Payment>, var itemClick : (position:Int)->Unit) : RecyclerView.Adapter<PaymentViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentViewHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.payment_item,parent,false)
        return PaymentViewHolder(v,itemClick)
    }

    override fun onBindViewHolder(holder: PaymentViewHolder, position: Int) {
        holder.bindData(liste.get(position))
    }

    override fun getItemCount(): Int {
        return liste.size
    }

}