package com.turkcell.payment_tracking_app.view

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.turkcell.payment_tracking_app.databinding.ActivityPaymentBinding
import com.turkcell.payment_tracking_app.model.PaymentType
import com.turkcell.payment_tracking_app.presenter.PaymentTrackingPresenter
import com.turkcell.payment_tracking_app.presenter.PaymentTrackingPresenterImpl
import java.util.*


class PaymentActivity : AppCompatActivity() {
    lateinit var binding : ActivityPaymentBinding
    private lateinit var ptPresenter: PaymentTrackingPresenter
    var paymentType : PaymentType? = null
    var paymentTypeId : Int? = null
    val calendar = Calendar.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initializeViews()

        binding.btnSetDate.setOnClickListener {
            setDateButtonClick()
        }

        binding.btnSave.setOnClickListener {
            onSavePaymentButtonClick()
        }
    }

    private fun initializeViews(){
        ptPresenter = PaymentTrackingPresenterImpl(this)
        ptPresenter.onAttach()
        paymentType = intent.getSerializableExtra("paymentType") as PaymentType?
        paymentTypeId = paymentType!!.id
        binding.tvDate.text = ptPresenter.getCurrentDate()
    }

    private fun onSavePaymentButtonClick() {
        if(ptPresenter.onSavePaymentCondition(paymentType!!,binding.tvDate.text.toString(),binding.edtPrice.text.toString())){
            val intentAddPayment = Intent()
            intentAddPayment.putExtra("paymentType", paymentType)
            setResult(RESULT_OK, intentAddPayment)
            finish()
        }else{
            Toast.makeText(this,"Ödeme tutarı tamsayı olmalıdır",Toast.LENGTH_SHORT).show()
        }
    }

    private fun setDateButtonClick(){
        datePickerDialogShow()
    }

    @SuppressLint("SetTextI18n")
    private fun datePickerDialogShow(){
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val dpd = DatePickerDialog(this, { view, year, monthOfYear, dayOfMonth ->
            binding.tvDate.text = "$dayOfMonth.${monthOfYear+1}.$year"
        }, year, month, day)
        dpd.getDatePicker().setMaxDate(System.currentTimeMillis());
        dpd.show()
    }

}