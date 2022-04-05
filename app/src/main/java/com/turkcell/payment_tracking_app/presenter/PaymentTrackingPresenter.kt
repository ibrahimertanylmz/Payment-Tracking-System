package com.turkcell.payment_tracking_app.presenter

import android.content.Intent
import com.turkcell.payment_tracking_app.model.Payment
import com.turkcell.payment_tracking_app.model.PaymentType
import com.turkcell.payment_tracking_app.model.Period

interface PaymentTrackingPresenter {
    fun onAttach()
    fun getPaymentTypes() : ArrayList<PaymentType>
    fun onSavePaymentCondition(paymentType: PaymentType, date: String, price: String)
    fun onAddPaymentTypeButtonClick(paymentType: PaymentType?, title: String, period: Period, periodDay: String, intent: Intent)
    fun deletePaymentType(paymentType: PaymentType)
    fun updatePaymentType(paymentType: PaymentType)
    fun addPaymentType(paymentType: PaymentType)
}