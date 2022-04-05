package com.turkcell.payment_tracking_app.presenter

import android.app.DatePickerDialog
import android.content.Context
import com.turkcell.payment_tracking_app.interactor.PaymentTrackingInteractor
import com.turkcell.payment_tracking_app.model.Payment
import com.turkcell.payment_tracking_app.model.PaymentType
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

    override fun onSetDateClick(): String {
        return showDatePickerDialog()
    }

    override fun onSavePaymentCondition(paymentType: PaymentType, date: String, price: String){
        val payment = Payment()
        payment.date = date
        payment.price = Integer.valueOf(price)
        paymentType.payments.add(payment)
        paymentType.id?.let { ptInteractor.addPayment(payment, it) }
    }

    private fun showDatePickerDialog(): String{
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        var selectedDate = ""

        val dpd = DatePickerDialog(context, { view, year, monthOfYear, dayOfMonth ->
            selectedDate = "" + dayOfMonth + "." + monthOfYear + "." + year
        }, year, month, day)
        dpd.getDatePicker().setMaxDate(System.currentTimeMillis());
        dpd.show()
        if (selectedDate!= "")
            return selectedDate
        return ""
    }

}