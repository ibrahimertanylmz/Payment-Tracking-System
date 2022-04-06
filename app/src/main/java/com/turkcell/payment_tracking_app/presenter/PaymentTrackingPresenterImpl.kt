package com.turkcell.payment_tracking_app.presenter

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.turkcell.payment_tracking_app.R
import com.turkcell.payment_tracking_app.adapter.PaymentAdapter
import com.turkcell.payment_tracking_app.interactor.PaymentTrackingInteractor
import com.turkcell.payment_tracking_app.interactor.PaymentTrackingInteractorImpl
import com.turkcell.payment_tracking_app.model.Payment
import com.turkcell.payment_tracking_app.model.PaymentType
import com.turkcell.payment_tracking_app.model.Period
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class PaymentTrackingPresenterImpl(internal var context: Context): PaymentTrackingPresenter {
    private lateinit var ptInteractor : PaymentTrackingInteractor
    override fun onAttach() {
        ptInteractor = PaymentTrackingInteractorImpl(context)
    }

    override fun getPaymentTypes() : ArrayList<PaymentType>{
        return ptInteractor.getPaymentTypes()
    }

    override fun onSavePaymentCondition(paymentType: PaymentType, date: String, price: String) : Boolean {
        if(checkPaymentPrice(price)){
            val payment = Payment()
            payment.date = date
            payment.price = Integer.valueOf(price)
            paymentType.payments.add(payment)
            paymentType.id?.let { ptInteractor.addPayment(payment, it) }
            return true
        }else{
            return false
        }
    }

    private fun checkPaymentPrice(price: String) : Boolean{
        return isNumber(price)
    }

    override fun onAddPaymentTypeButtonClick(paymentType: PaymentType?, title: String, period: Period, periodDay: String, intent: Intent,context: Context) : Boolean {
        var isValid = false
        if(checkPaymentTypeValues(title, period, periodDay,context)){
            isValid = true
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
        return isValid
    }

    override fun onPaymentItemClick(position: Int, payments: ArrayList<Payment>, adapter: PaymentAdapter, context: Context) {
        showAlertDeletePayment(position,payments,adapter,context)
    }

    override fun onPaymentTypeItemClick(position: Int, paymentTypeList: ArrayList<PaymentType>) {
        if(paymentTypeList.get(position).id!= null){
            paymentTypeList.get(position).payments = ptInteractor.getPaymentsWithId(paymentTypeList.get(position).id)
        }
    }

    override fun onDeletePaymentCondition(paymentType: PaymentType) {
        deletePaymentType(paymentType)
    }

    override fun onResultPaymentAdded(paymentType: PaymentType, updatedPaymentType: PaymentType, payments: ArrayList<Payment>) {
        payments.clear()
        payments.addAll(updatedPaymentType.payments)
        paymentType.title = updatedPaymentType.title
        paymentType.period = updatedPaymentType.period
        paymentType.periodDay = updatedPaymentType.periodDay
    }

    @SuppressLint("SimpleDateFormat")
    override fun getCurrentDate(): String {
        val date = Calendar.getInstance().time
        val sdf = SimpleDateFormat("dd.MM.yyyy")
        return sdf.format(date)
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
    private fun deletePayment(paymentType: Payment){
        ptInteractor.deletePayment(paymentType.id!!)
    }

    private fun checkPaymentTypeValues(title: String, period: Period, periodDay: String, context: Context) : Boolean{
        if(title.isEmpty() || title.isBlank()){
            Toast.makeText(context, context.getString(R.string.empty_title_toast), Toast.LENGTH_SHORT).show()
        }else if(!isNumber(periodDay)){
            Toast.makeText(context, context.getString(R.string.period_not_number_toast), Toast.LENGTH_SHORT).show()
        }else if(period == Period.Haftalık){
            if (Integer.valueOf(periodDay) > 7 || Integer.valueOf(periodDay) < 1){
                Toast.makeText(context, context.getString(R.string.weekly_period_day_toast), Toast.LENGTH_SHORT).show()
            }else{
                return true
            }
        }else if (period == Period.Aylık){
            if (Integer.valueOf(periodDay) > 31 || Integer.valueOf(periodDay) < 1){
                Toast.makeText(context, context.getString(R.string.mothly_period_day_toast), Toast.LENGTH_SHORT).show()
            }else{
                return true
            }
        }else if (period == Period.Yıllık){
            if (Integer.valueOf(periodDay) > 365 || Integer.valueOf(periodDay) < 1){
                Toast.makeText(context, context.getString(R.string.yearly_period_day_toast), Toast.LENGTH_SHORT).show()
            }else{
                return true
            }
        }
        return false
    }

    private fun isNumber(s: String): Boolean {
        return try {
            s.toInt()
            true
        } catch (ex: NumberFormatException) {
            false
        }
    }

    private fun showAlertDeletePayment(position: Int, payments: ArrayList<Payment>, adapter: PaymentAdapter, context: Context): Boolean? {
        var deleteItem : Boolean? = null
        val adb : AlertDialog.Builder = AlertDialog.Builder(context)
        adb.setTitle("SİL")
        adb.setMessage("Ödemeyi Silmek İstediğinize Emin Misiniz?")
        adb.setPositiveButton("Sil", DialogInterface.OnClickListener { dialog, which ->
            deletePayment(payments.get(position))
            payments.removeAt(position)
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