package com.turkcell.payment_tracking_app.interactor

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.turkcell.payment_tracking_app.database.DatabaseOpenHelper
import com.turkcell.payment_tracking_app.model.Payment
import com.turkcell.payment_tracking_app.model.PaymentType
import com.turkcell.payment_tracking_app.model.Period

class PaymentTrackingInteractorImpl(context: Context): PaymentTrackingInteractor {
    //PRAGMA foreign_keys = ON
    private var PaymentTypeDatabase : SQLiteDatabase? = null
    private var dbOpenHelper : DatabaseOpenHelper

    init {
        dbOpenHelper = DatabaseOpenHelper(context,"PaymentTypeDb", null,1)
    }

    private fun open(){
        PaymentTypeDatabase = dbOpenHelper.writableDatabase
    }

    private fun close(){
        if(PaymentTypeDatabase != null && PaymentTypeDatabase!!.isOpen){
            PaymentTypeDatabase!!.close()
        }
    }

    override fun addPaymentType(paymentType: PaymentType): Long{
        val cv = ContentValues()
        cv.put("Title",paymentType.title)
        cv.put("Period", paymentType.period!!.name)
        cv.put("PeriodDay", paymentType.periodDay)

        open()
        val etkilenenKayit = PaymentTypeDatabase!!.insert("PaymentType", null, cv)
        close()
        return etkilenenKayit
    }


    override fun addPayment(payment: Payment, paymentTypeId: Int): Long{
        val cv = ContentValues()
        cv.put("Date",payment.date)
        cv.put("Price", payment.price)
        cv.put("PaymentTypeId", paymentTypeId)

        open()
        val etkilenenKayit = PaymentTypeDatabase!!.insert("Payment", null, cv)
        close()
        return etkilenenKayit
    }


    override fun updatePaymentType(paymentType: PaymentType){
        val cv = ContentValues()
        cv.put("Title",paymentType.title)
        cv.put("Period", paymentType.period!!.name)
        cv.put("PeriodDay", paymentType.periodDay)

        open()
        PaymentTypeDatabase!!.update("PaymentType", cv, "Id = ?", arrayOf(paymentType.id.toString()))
        close()
    }

    override fun deletePaymentType(id: Int){
        open()
        PaymentTypeDatabase!!.delete("PaymentType", "Id = ?", arrayOf(id.toString()))
        close()

    }

    override fun deletePayment(id: Int){
        open()
        PaymentTypeDatabase!!.delete("Payment", "Id = ?", arrayOf(id.toString()))
        close()

    }

    private fun getAllPaymentTypes() : Cursor {
        val sorgu = "Select * from PaymentType"
        return PaymentTypeDatabase!!.rawQuery(sorgu, null)
    }

    private fun getPaymentsOfPaymentType(id: Int) : Cursor {
        val sorgu = "Select * from Payment where PaymentTypeId = '$id'"
        return PaymentTypeDatabase!!.rawQuery(sorgu, null)
    }

    @SuppressLint("Range")
    override fun getPaymentTypes(): ArrayList<PaymentType>{
        val paymentTypeList = ArrayList<PaymentType>()
        var paymentType : PaymentType
        open()
        var cursor : Cursor = getAllPaymentTypes()
        if(cursor.moveToFirst()){

            do {
                paymentType = PaymentType(cursor.getString(cursor.getColumnIndex("Title")))
                paymentType.id = cursor.getInt(0)
                if( cursor.getString(cursor.getColumnIndex("Period")) == "Haftalık"){
                    paymentType.period = Period.Haftalık
                }else if ( cursor.getString(cursor.getColumnIndex("Period")) == "Aylık"){
                    paymentType.period = Period.Aylık
                }else{
                    paymentType.period = Period.Yıllık
                }
                paymentType.periodDay =  cursor.getInt(cursor.getColumnIndex("PeriodDay"))

                paymentTypeList.add(paymentType)
            }while (cursor.moveToNext())

        }

        close()
        return paymentTypeList
    }

    @SuppressLint("Range")
    override fun getPaymentsWithId(id: Int?): ArrayList<Payment>{
        val paymentList = ArrayList<Payment>()
        var payment : Payment
        open()

        var cursor : Cursor = getPaymentsOfPaymentType(id!!)
        if(cursor.moveToFirst()){

            do {
                payment = Payment()
                payment.id = cursor.getInt(0)
                payment.date =  cursor.getString(cursor.getColumnIndex("Date"))
                payment.price =  cursor.getInt(cursor.getColumnIndex("Price"))

                paymentList.add(payment)
            }while (cursor.moveToNext())

        }
        close()
        return paymentList
    }
}