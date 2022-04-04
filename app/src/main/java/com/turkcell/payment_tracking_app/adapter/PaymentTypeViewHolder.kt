package com.turkcell.payment_tracking_app.adapter

import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.turkcell.payment_tracking_app.R
import com.turkcell.payment_tracking_app.model.PaymentType

class PaymentTypeViewHolder(itemView: View, var itemClick : (position:Int)->Unit, var addButtonClick : (position:Int)->Unit) : RecyclerView.ViewHolder (itemView) {

    var tvTitle : TextView
    var tvPeriod : TextView
    var tvPeriodDay : TextView
    var btnAddPayment : Button

    init {
        tvTitle = itemView.findViewById(R.id.tvTitle)
        tvPeriod = itemView.findViewById(R.id.tvPeriod)
        tvPeriodDay = itemView.findViewById(R.id.tvPeriodDay)
        btnAddPayment = itemView.findViewById(R.id.btnAddPayment)

        btnAddPayment.setOnClickListener {
            addButtonClick(adapterPosition)
        }
        itemView.setOnClickListener {
            itemClick(adapterPosition)
        }
    }

    fun bindData(paymentType: PaymentType){
        tvTitle.text = paymentType.title
        if (paymentType.period != null){
        tvPeriod.text = paymentType.period!!.name
        }
        if (paymentType.periodDay!= null){
            tvPeriodDay.text = paymentType.periodDay.toString()
        }
    }
}