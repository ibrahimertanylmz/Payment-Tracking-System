package com.turkcell.payment_tracking_app.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import com.turkcell.payment_tracking_app.R
import com.turkcell.payment_tracking_app.interactor.PaymentTrackingInteractor
import com.turkcell.payment_tracking_app.databinding.ActivityPaymentTypeBinding
import com.turkcell.payment_tracking_app.model.PaymentType
import com.turkcell.payment_tracking_app.model.Period
import com.turkcell.payment_tracking_app.presenter.PaymentTrackingPresenter
import com.turkcell.payment_tracking_app.presenter.PaymentTrackingPresenterImpl

class PaymentTypeActivity : AppCompatActivity() {
    lateinit var binding : ActivityPaymentTypeBinding
    var paymentType : PaymentType? = null
    internal lateinit var ptPresenter: PaymentTrackingPresenter
    val po = PaymentTrackingInteractor(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentTypeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializeViews()

        binding.btnAdd.setOnClickListener {
            onAddButtonClick()
        }

        binding.btnDelete.setOnClickListener {
            onDeleteButtonClick()
        }
    }
    private fun initializeViews(){
        ptPresenter = PaymentTrackingPresenterImpl(this)
        ptPresenter.onAttach()
        paymentType = intent.getSerializableExtra("paymentType") as PaymentType?
        val adapter : ArrayAdapter<Period> = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, enumValues<Period>())
        binding.spinnerPeriod.adapter = adapter

        if (paymentType!= null){
            binding.edtTitle.setText(paymentType!!.title)
            binding.edtPeriodDay.setText(paymentType!!.periodDay.toString())
            binding.spinnerPeriod.setSelection(adapter.getPosition(paymentType!!.period))
            binding.btnDelete.visibility = View.VISIBLE
            binding.btnAdd.text = (getString(R.string.txt_edit))
        }
    }

    private fun onAddButtonClick(){
        val intentAddPaymentType = Intent()
        ptPresenter.onAddPaymentTypeButtonClick(paymentType,binding.edtTitle.text.toString(),binding.spinnerPeriod.selectedItem as Period, binding.edtPeriodDay.text.toString(), intentAddPaymentType)
        setResult(RESULT_OK, intentAddPaymentType)
        finish()
    }

    private fun onDeleteButtonClick() {
        ptPresenter.onDeletePaymentCondition(paymentType!!)
        val intentAddPaymentType = Intent()
        intentAddPaymentType.putExtra("paymentType", paymentType)
        intentAddPaymentType.putExtra("isDeleted", true)
        setResult(RESULT_OK, intentAddPaymentType)
        finish()
    }
}