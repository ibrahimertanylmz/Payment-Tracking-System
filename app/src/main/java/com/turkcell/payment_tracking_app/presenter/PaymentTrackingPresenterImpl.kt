package com.turkcell.payment_tracking_app.presenter

import android.app.DatePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.turkcell.payment_tracking_app.adapter.PaymentAdapter
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



    override fun onPaymentItemClick(position: Int, payments: ArrayList<Payment>, adapter: PaymentAdapter, context: Context) {

    }

    override fun onDeletePaymentCondition(paymentType: PaymentType) {
        deletePaymentType(paymentType)
    }

    private fun addPaymentType(paymentType: PaymentType){
        ptInteractor.addPaymentType(paymentType)
    }

    private fun updatePaymentType(paymentType: PaymentType){
        ptInteractor.updatePaymentType(paymentType)
    }

    private fun deletePaymentType(paymentType: PaymentType){
        ptInteractor.deletePaymentType(paymentType.id!!)
    }

    private fun showAlertDeletePayment(position: Int, payments: ArrayList<Payment>, adapter: PaymentAdapter, context: Context): Boolean? {
        var deleteItem : Boolean? = null
        val adb : AlertDialog.Builder = AlertDialog.Builder(context)
        adb.setTitle("SİL")
        adb.setMessage("Ödemeyi Silmek İstediğinize Emin Misiniz?")
        adb.setPositiveButton("Sil", DialogInterface.OnClickListener { dialog, which ->
            payments.get(position).id?.let { ptInteractor.deletePayment(it) }
            payments.removeAt(position)
            //binding.rwPayments.adapter = PaymentAdapter(this, payments, ::itemClick)
            adapter.notifyDataSetChanged()
            deleteItem = true
        })
        adb.setNegativeButton("Vazgeç", DialogInterface.OnClickListener { dialog, which ->
            deleteItem = false
        })
        val uyari : AlertDialog = adb.create()
        uyari.show()
        return deleteItem
    }


}