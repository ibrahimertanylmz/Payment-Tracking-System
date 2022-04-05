package com.turkcell.payment_tracking_app.presenter

import com.turkcell.payment_tracking_app.model.Payment
import com.turkcell.payment_tracking_app.model.PaymentType

interface PaymentTrackingPresenter {
    fun onAttach()
    fun getPaymentTypes() : ArrayList<PaymentType>
    fun onSetDateClick() : String
    fun onSavePaymentCondition(paymentType: PaymentType, date: String, price: String)
}