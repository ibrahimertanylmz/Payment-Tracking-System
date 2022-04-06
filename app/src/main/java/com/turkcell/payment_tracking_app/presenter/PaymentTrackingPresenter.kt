package com.turkcell.payment_tracking_app.presenter

import android.content.AbstractThreadedSyncAdapter
import android.content.Context
import android.content.Intent
import com.turkcell.payment_tracking_app.adapter.PaymentAdapter
import com.turkcell.payment_tracking_app.model.Payment
import com.turkcell.payment_tracking_app.model.PaymentType
import com.turkcell.payment_tracking_app.model.Period

interface PaymentTrackingPresenter {
    fun onAttach()
    fun getPaymentTypes() : ArrayList<PaymentType>
    fun onSavePaymentCondition(paymentType: PaymentType, date: String, price: String)
    fun onAddPaymentTypeButtonClick(paymentType: PaymentType?, title: String, period: Period, periodDay: String, intent: Intent, context: Context) : Boolean
    fun onPaymentItemClick(position: Int, payments: ArrayList<Payment>, adapter: PaymentAdapter, context: Context)
    fun onDeletePaymentCondition(paymentType: PaymentType)
}