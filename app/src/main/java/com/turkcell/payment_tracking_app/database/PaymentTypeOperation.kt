package com.turkcell.payment_tracking_app.database

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.turkcell.payment_tracking_app.model.PaymentType
import com.turkcell.payment_tracking_app.model.Period

class PaymentTypeOperation(context: Context) {
    var PaymentTypeDatabase : SQLiteDatabase? = null
    var dbOpenHelper : DatabaseOpenHelper

    init {
        dbOpenHelper = DatabaseOpenHelper(context,"PaymentTypeDb", null,1)
    }

    fun open(){
        PaymentTypeDatabase = dbOpenHelper.writableDatabase
    }

    fun close(){
        if(PaymentTypeDatabase != null && PaymentTypeDatabase!!.isOpen){
            PaymentTypeDatabase!!.close()
        }
    }

    fun addPaymentType(paymentType: PaymentType): Long{
        val cv = ContentValues()
        cv.put("Title",paymentType.title)
        cv.put("Period", paymentType.period!!.name)
        cv.put("PeriodDay", paymentType.periodDay)

        open()
        val etkilenenKayit = PaymentTypeDatabase!!.insert("PaymentType", null, cv)
        close()
        return etkilenenKayit
    }

    fun updatePaymentType(paymentType: PaymentType){
        val cv = ContentValues()
        cv.put("Title",paymentType.title)
        cv.put("Period", paymentType.period!!.name)
        cv.put("PeriodDay", paymentType.periodDay)

        open()
        //YapilacakDatabase!!.update("Yapilacak", cv, "Id = 5", null)
        PaymentTypeDatabase!!.update("PaymentType", cv, "Id = ?", arrayOf(paymentType.id.toString()))
        //YapilacakDatabase!!.update("Yapilacak", cv, "Id = ? and Baslik = ?", arrayOf(yapilacak.id.toString(), yapilacak.Baslik)) .. sırasıyla olmalı!
        //YapilacakDatabase!!.update("Yapilacak", cv, "Id = ${yapilacak.id}", arrayOf(yapilacak.id.toString()))
        close()
    }

    fun deletePaymentType(id: Int){
        open()
        PaymentTypeDatabase!!.delete("PaymentType", "Id = ?", arrayOf(id.toString()))
        close()
    }

    private fun getAllPaymentTypes() : Cursor {
        val sorgu = "Select * from PaymentType"
        return PaymentTypeDatabase!!.rawQuery(sorgu, null)
    }

     /*private fun tumYapilacaklarGetirBaslik(baslik: String) : Cursor {
        val sorgu = "Select * from Yapilaca Where Baslik =?"
        return PaymentTypeDatabase!!.rawQuery(sorgu, arrayOf(baslik))
    }*/

    @SuppressLint("Range")
    fun getPaymentTypes(): ArrayList<PaymentType>{
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
}