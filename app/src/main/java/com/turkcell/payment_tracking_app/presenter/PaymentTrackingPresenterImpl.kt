package com.turkcell.payment_tracking_app.presenter

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.turkcell.payment_tracking_app.interactor.PaymentTrackingInteractor
import com.turkcell.payment_tracking_app.model.Payment
import com.turkcell.payment_tracking_app.model.PaymentType
import com.turkcell.payment_tracking_app.model.Period
import java.util.*
import kotlin.collections.ArrayList


class PaymentTrackingPresenterImpl(internal var context: Context): PaymentTrackingPresenter {
    internal lateinit var ptInteractor : PaymentTrackingInteractor
    override fun onAttach() {
        ptInteractor = PaymentTrackingInteractor(context)
    }

    override fun getPaymentTypes() : ArrayList<PaymentType>{
        return ptInteractor.getPaymentTypes()
    }

    override fun onSavePaymentCondition(paymentType: PaymentType, date: String, price: String){
        val payment = Payment()
        payment.date = date
        payment.price = Integer.valueOf(price)
        paymentType.payments.add(payment)
        paymentType.id?.let { ptInteractor.addPayment(payment, it) }
    }

    override fun onAddPaymentTypeButtonClick(paymentType: PaymentType?, title: String, period: Period, periodDay: String, intent: Intent) {
        var isExisting = true
        var newPaymentType = PaymentType("")
        if (paymentType == null) {
            isExisting = false
            newPaymentType = PaymentType(title)
            newPaymentType.period = period
            newPaymentType.periodDay = Integer.valueOf(periodDay)
        }else{
            paymentType.title = title
            paymentType.period = period
            paymentType.periodDay = Integer.valueOf(periodDay)
        }

        if(!isExisting){
            addPaymentType(newPaymentType)
            intent.putExtra("paymentType", newPaymentType)
        }else{
            updatePaymentType(paymentType!!)
            intent.putExtra("paymentType", paymentType)
        }
    }

    override fun addPaymentType(paymentType: PaymentType){
        ptInteractor.addPaymentType(paymentType)
    }

    override fun updatePaymentType(paymentType: PaymentType){
        ptInteractor.updatePaymentType(paymentType)
    }

    override fun deletePaymentType(paymentType: PaymentType){
        ptInteractor.deletePayment(paymentType.id!!)
    }


}