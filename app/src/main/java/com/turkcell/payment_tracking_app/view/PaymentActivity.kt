package com.turkcell.payment_tracking_app.view

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.turkcell.payment_tracking_app.interactor.PaymentTrackingInteractor
import com.turkcell.payment_tracking_app.databinding.ActivityPaymentBinding
import com.turkcell.payment_tracking_app.model.Payment
import com.turkcell.payment_tracking_app.model.PaymentType
import com.turkcell.payment_tracking_app.presenter.PaymentTrackingPresenter
import com.turkcell.payment_tracking_app.presenter.PaymentTrackingPresenterImpl
import java.text.SimpleDateFormat
import java.util.*


class PaymentActivity : AppCompatActivity() {
    lateinit var binding : ActivityPaymentBinding
    internal lateinit var ptPresenter: PaymentTrackingPresenter
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
        binding.tvDate.text = ("" +calendar.get(Calendar.DAY_OF_MONTH) +"."+ calendar.get(Calendar.MONTH) +"."+ calendar.get(Calendar.YEAR)) // simpleDateFormat ile yaparsan daha mantıklı
    }

    private fun onSavePaymentButtonClick() {
        ptPresenter.onSavePaymentCondition(paymentType!!,binding.tvDate.text.toString(),binding.edtPrice.text.toString())
        val intentAddPayment = Intent()
        intentAddPayment.putExtra("paymentType", paymentType)
        setResult(RESULT_OK, intentAddPayment)
        finish()
    }

    @SuppressLint("SetTextI18n")
    private fun setDateButtonClick(){
        datePickerDialogShow()
    }

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