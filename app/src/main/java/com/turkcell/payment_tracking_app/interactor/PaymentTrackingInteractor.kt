package com.turkcell.payment_tracking_app.interactor

import com.turkcell.payment_tracking_app.model.Payment
import com.turkcell.payment_tracking_app.model.PaymentType

interface PaymentTrackingInteractor {
    fun addPaymentType(paymentType: PaymentType): Long
    fun addPayment(payment: Payment, paymentTypeId: Int): Long
    fun updatePaymentType(paymentType: PaymentType)
    fun deletePaymentType(id: Int)
    fun deletePayment(id: Int)
    fun getPaymentTypes(): ArrayList<PaymentType>
    fun getPaymentsWithId(id: Int?): ArrayList<Payment>
}