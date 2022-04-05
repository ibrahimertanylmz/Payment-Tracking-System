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

class PaymentTypeActivity : AppCompatActivity() {
    lateinit var binding : ActivityPaymentTypeBinding
    var paymentType : PaymentType? = null
    internal lateinit var ptPresenter: PaymentTrackingPresenter
    val po = PaymentTrackingInteractor(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentTypeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        paymentType = intent.getSerializableExtra("paymentType") as PaymentType?

        //val genders = enumValues<Period>() // bunu dene

        val pers = enumValues<Period>()


        val adap : ArrayAdapter<Period> = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, pers)
        binding.spinnerPeriod.adapter = adap

        if (paymentType!= null){
            binding.edtTitle.setText(paymentType!!.title)
            binding.edtPeriodDay.setText(paymentType!!.periodDay.toString())
            binding.spinnerPeriod.setSelection(adap.getPosition(paymentType!!.period))
            binding.btnDelete.visibility = View.VISIBLE
        }


        binding.btnAdd.setOnClickListener {

            var nullKeep = false
            if (paymentType == null) {
                nullKeep = true
                paymentType = PaymentType(binding.edtTitle.text.toString())
            }else{
                paymentType!!.title = binding.edtTitle.text.toString()
            }

            //paymentType = PaymentType(binding.edtTitle.text.toString())
            paymentType!!.period = binding.spinnerPeriod.selectedItem as Period?
            paymentType!!.periodDay = Integer.valueOf(binding.edtPeriodDay.text.toString())

            val intentAddPaymentType = Intent()
            intentAddPaymentType.putExtra("paymentType", paymentType)


            if(nullKeep== true){
                po.addPaymentType(paymentType!!)
            }else{
                po.updatePaymentType(paymentType!!)
            }


            setResult(RESULT_OK, intentAddPaymentType)
            finish()
        }

        binding.btnDelete.setOnClickListener {
            // SİLİNECEK
            val intentAddPaymentType = Intent()
            intentAddPaymentType.putExtra("paymentType", paymentType)
            intentAddPaymentType.putExtra("isDeleted", true)
            setResult(RESULT_OK, intentAddPaymentType)
            finish()

            paymentType!!.id?.let { it1 -> po.deletePaymentType(it1) }
        }
    }
    private fun initializeViews(){

    }
}