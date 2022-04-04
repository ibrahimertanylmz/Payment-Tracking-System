package com.turkcell.payment_tracking_app.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

//veritabanı ve tablolar ile ilgili yapısal işlemler yapılır
class DatabaseOpenHelper(context: Context, name: String, factory: SQLiteDatabase.CursorFactory?, version: Int) : SQLiteOpenHelper(context, name, factory, version){
    override fun onCreate(db: SQLiteDatabase) {
        val sorgu = "CREATE TABLE PaymentType(Id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, Title TEXT, Period TEXT, PeriodDay INTEGER)"
        db.execSQL(sorgu)
        val sorgu2 = "CREATE TABLE Payment(Id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, Date TEXT, Price TEXT, PaymentTypeId INTEGER, FOREIGN KEY(PaymentTypeId) REFERENCES PaymentType(Id))"
        db.execSQL(sorgu2)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if (oldVersion == 1){ //tüm versiyonlar için ayrı işlemler yapılmalı

        }else if(oldVersion == 2){

        }
    }


}


///!!!
///update ederken         cv.put("Icerik", yapilacak.Icerik)
// /insert ederken         cv.put("(fk)PaymentTypeId", paymentType.Id)
//  ON DELETE CASCADE --> foreign keyin oldugu tabloya sona ekle, paymentType silinirse paymentlar otomatik olarak silinir