package com.turkcell.payment_tracking_app.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import com.turkcell.payment_tracking_app.adapter.PaymentAdapter
import com.turkcell.payment_tracking_app.databinding.ActivityPaymentTypeDetailsBinding
import com.turkcell.payment_tracking_app.model.Payment
import com.turkcell.payment_tracking_app.model.PaymentType
import com.turkcell.payment_tracking_app.presenter.PaymentTrackingPresenter
import com.turkcell.payment_tracking_app.presenter.PaymentTrackingPresenterImpl

class PaymentTypeDetailsActivity : AppCompatActivity() {
    lateinit var binding : ActivityPaymentTypeDetailsBinding
    var paymentType : PaymentType? = null
    var payments = ArrayList<Payment>()
    private lateinit var ptPresenter: PaymentTrackingPresenter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentTypeDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializeViews()

        binding.btnAddNewPayment.setOnClickListener {
            val intent = Intent(this, PaymentActivity::class.java)
            intent.putExtra("paymentType", paymentType)
            resultLauncher.launch(intent)
        }

        binding.btnBack.setOnClickListener {
            val intentAddPaymentType = Intent()
            paymentType!!.payments = payments
            intentAddPaymentType.putExtra("paymentType", paymentType)
            setResult(RESULT_OK, intentAddPaymentType)
            finish()
        }

        binding.btnEdit.setOnClickListener {
            val intent = Intent(this, PaymentTypeActivity::class.java)
            intent.putExtra("paymentType", paymentType)
            resultLauncher.launch(intent)
        }

    }

    private var resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    val gelenData: Intent? = result.data
                    val updatedPaymentType = gelenData!!.getSerializableExtra("paymentType") as PaymentType
                    val isDeleted = gelenData.getBooleanExtra("isDeleted",false)
                    if(isDeleted){
                        val intentAddPaymentType = Intent()
                        setResult(RESULT_OK, intentAddPaymentType)
                        finish()
                    }else{
                        ptPresenter.onResultPaymentAdded(paymentType!!,updatedPaymentType,payments)
                        updateViewsOnResult()
                    }
                }
            }

    private fun initializeViews(){
        ptPresenter = PaymentTrackingPresenterImpl(this)
        ptPresenter.onAttach()

        paymentType = intent.getSerializableExtra("paymentType") as PaymentType?
        payments = paymentType!!.payments
        binding.tvpaymentTypeTitle.text = paymentType!!.title
        binding.tvPaymentTypePeriod.text = paymentType!!.period!!.name
        binding.tvPaymentPeriodDay.text = paymentType!!.periodDay.toString()

        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.rwPayments.layoutManager = layoutManager
        binding.rwPayments.adapter = PaymentAdapter(this, payments, ::itemClick)
    }

    private fun updateViewsOnResult() {
        binding.rwPayments.adapter = PaymentAdapter(this, payments, ::itemClick)
        binding.tvpaymentTypeTitle.text = paymentType!!.title
        binding.tvPaymentTypePeriod.text = paymentType!!.period!!.name
        binding.tvPaymentPeriodDay.text = paymentType!!.periodDay.toString()
    }

    private fun itemClick(position: Int) {
        ptPresenter.onPaymentItemClick(position,payments, binding.rwPayments.adapter as PaymentAdapter, this)
    }
}