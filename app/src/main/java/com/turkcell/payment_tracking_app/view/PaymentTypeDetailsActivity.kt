package com.turkcell.payment_tracking_app.view

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.turkcell.payment_tracking_app.adapter.PaymentAdapter
import com.turkcell.payment_tracking_app.interactor.PaymentTrackingInteractor
import com.turkcell.payment_tracking_app.databinding.ActivityPaymentTypeDetailsBinding
import com.turkcell.payment_tracking_app.model.Payment
import com.turkcell.payment_tracking_app.model.PaymentType

class PaymentTypeDetailsActivity : AppCompatActivity() {
    lateinit var binding : ActivityPaymentTypeDetailsBinding
    var paymentType : PaymentType? = null
    var payments = ArrayList<Payment>()
    val po = PaymentTrackingInteractor(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentTypeDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        paymentType = intent.getSerializableExtra("paymentType") as PaymentType?
        payments = paymentType!!.payments

        binding.tvpaymentTypeTitle.text = paymentType!!.title
        binding.tvPaymentTypePeriod.text = paymentType!!.period!!.name
        binding.tvPaymentPeriodDay.text = paymentType!!.periodDay.toString()

        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.rwPayments.layoutManager = layoutManager
        binding.rwPayments.adapter = PaymentAdapter(this, payments, ::itemClick)


        binding.btnAddNewPayment.setOnClickListener {
            val intent = Intent(this, PaymentActivity::class.java)
            intent.putExtra("paymentType", paymentType)
            resultLauncher.launch(intent)
        }

        binding.btnBack.setOnClickListener {
            val intentAddPaymentType = Intent()
            paymentType!!.payments = payments
            println(paymentType!!.payments)
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

    var resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_CANCELED) {
                    Toast.makeText(this, "No Changes!", Toast.LENGTH_SHORT).show()
                }
                if (result.resultCode == RESULT_OK) {
                    Toast.makeText(this, "Changes Saved Successfully!", Toast.LENGTH_SHORT).show()
                    val gelenData: Intent? = result.data
                    val paymentTypePlus = gelenData!!.getSerializableExtra("paymentType") as PaymentType
                    val isDeleted = gelenData.getBooleanExtra("isDeleted",false)

                    if(isDeleted){
                        val intentAddPaymentType = Intent()
                        intentAddPaymentType.putExtra("paymentType", paymentType)
                        intentAddPaymentType.putExtra("isDeleted",isDeleted)
                        setResult(RESULT_OK, intentAddPaymentType)
                        finish()
                    }else{
                        payments = paymentTypePlus.payments
                        paymentType = paymentTypePlus
                        binding.rwPayments.adapter = PaymentAdapter(this, payments, ::itemClick)

                        binding.tvpaymentTypeTitle.text = paymentType!!.title
                        binding.tvPaymentTypePeriod.text = paymentType!!.period!!.name
                        binding.tvPaymentPeriodDay.text = paymentType!!.periodDay.toString()
                    }

                }
            }

    private fun itemClick(position: Int) {
        uyariGoster(position)
    }
    fun uyariGoster(position: Int): Boolean? {
        var deleteItem : Boolean? = null
        val adb : AlertDialog.Builder = AlertDialog.Builder(this)
        adb.setTitle("SİL")
        adb.setMessage("Ödemeyi Silmek İstediğinize Emin Misiniz?")
        adb.setPositiveButton("Sil", DialogInterface.OnClickListener { dialog, which ->
            payments.get(position).id?.let { po.deletePayment(it) }
            payments.removeAt(position)
            binding.rwPayments.adapter = PaymentAdapter(this, payments, ::itemClick)
            deleteItem = true
        })
        adb.setNegativeButton("Vazgeç", DialogInterface.OnClickListener { dialog, which ->
            deleteItem = false
        })
        val uyari : AlertDialog = adb.create()
        uyari.show()
        return deleteItem
    }
}