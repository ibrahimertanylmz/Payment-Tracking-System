package com.turkcell.payment_tracking_app.view

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.turkcell.payment_tracking_app.database.PaymentTypeOperation
import com.turkcell.payment_tracking_app.databinding.ActivityPaymentBinding
import com.turkcell.payment_tracking_app.model.Payment
import com.turkcell.payment_tracking_app.model.PaymentType
import java.util.*


class PaymentActivity : AppCompatActivity() {
    lateinit var binding : ActivityPaymentBinding
    var paymentType : PaymentType? = null
    var year: Int? = null
    var month: Int? = null
    var day: Int? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        paymentType = intent.getSerializableExtra("paymentType") as PaymentType?

        binding.btnSetDate.setOnClickListener {
            val c = Calendar.getInstance()
            year = c.get(Calendar.YEAR)
            month = c.get(Calendar.MONTH)
            day = c.get(Calendar.DAY_OF_MONTH)


            val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                // Display Selected date in textbox
                binding.tvDate.setText("" + dayOfMonth + "." + monthOfYear + "." + year)

            }, year!!, month!!, day!!)

            // binding.tvDate.text = System.currentTimeMillis().toString() o anki tarihi ayarla
            dpd.getDatePicker().setMaxDate(System.currentTimeMillis());
            dpd.show()
        }

        binding.btnSave.setOnClickListener {
            val payment = Payment()
            payment.date = binding.tvDate.text.toString()
            payment.price = Integer.valueOf(binding.edtPrice.text.toString())
            paymentType!!.payments.add(payment)
            println(payment)
            val intentAddPayment = Intent()
            intentAddPayment.putExtra("paymentType", paymentType)
            setResult(RESULT_OK, intentAddPayment)
            finish()
        }

    }
}