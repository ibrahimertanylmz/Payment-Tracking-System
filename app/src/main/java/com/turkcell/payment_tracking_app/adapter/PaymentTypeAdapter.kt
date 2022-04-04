package com.turkcell.payment_tracking_app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.turkcell.payment_tracking_app.R
import com.turkcell.payment_tracking_app.model.PaymentType

class PaymentTypeAdapter(var context: Context, var liste: ArrayList<PaymentType>, var itemClick : (position:Int)->Unit, var addButtonClick : (position:Int)->Unit) : RecyclerView.Adapter<PaymentTypeViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentTypeViewHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.payment_type_item,parent,false)
        return PaymentTypeViewHolder(v,itemClick,addButtonClick)
    }

    override fun onBindViewHolder(holder: PaymentTypeViewHolder, position: Int) {
        holder.bindData(liste.get(position))
    }

    override fun getItemCount(): Int {
        return liste.size
    }
}