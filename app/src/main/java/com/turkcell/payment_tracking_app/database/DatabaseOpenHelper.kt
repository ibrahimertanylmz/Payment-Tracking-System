package com.turkcell.payment_tracking_app.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseOpenHelper(context: Context, name: String, factory: SQLiteDatabase.CursorFactory?, version: Int) : SQLiteOpenHelper(context, name, factory, version){
    override fun onCreate(db: SQLiteDatabase) {
        val sorguForeingKey = "PRAGMA foreign_keys = ON"
        db!!.execSQL(sorguForeingKey)
        val sorguPaymentType = "CREATE TABLE PaymentType(Id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, Title TEXT, Period TEXT, PeriodDay INTEGER)"
        db.execSQL(sorguPaymentType)
        val sorguPayment = "CREATE TABLE Payment(Id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, Date TEXT, Price INTEGER, PaymentTypeId INTEGER, FOREIGN KEY(PaymentTypeId) REFERENCES PaymentType(Id) ON DELETE CASCADE)"
        db.execSQL(sorguPayment)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if (oldVersion == 1){

        }
    }


}
