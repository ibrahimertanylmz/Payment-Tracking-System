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
    fun onSavePaymentCondition(paymentType: PaymentType, date: String, price: String): Boolean
    fun onAddPaymentTypeButtonClick(paymentType: PaymentType?, title: String, period: Period, periodDay: String, intent: Intent, context: Context) : Boolean
    fun onPaymentItemClick(position: Int, payments: ArrayList<Payment>, adapter: PaymentAdapter, context: Context)
    fun onPaymentTypeItemClick(position: Int, paymentTypeList: ArrayList<PaymentType>)
    fun onDeletePaymentCondition(paymentType: PaymentType)
    fun onResultPaymentAdded(paymentType: PaymentType, updatedPaymentType: PaymentType, payments: ArrayList<Payment>)
    fun getCurrentDate(): String
}