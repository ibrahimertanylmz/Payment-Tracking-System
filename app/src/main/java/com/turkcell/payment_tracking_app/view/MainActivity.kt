package com.turkcell.payment_tracking_app.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import com.turkcell.payment_tracking_app.R
import com.turkcell.payment_tracking_app.adapter.PaymentTypeAdapter
import com.turkcell.payment_tracking_app.database.PaymentTypeOperation
import com.turkcell.payment_tracking_app.databinding.ActivityMainBinding
import com.turkcell.payment_tracking_app.databinding.ActivityPaymentTypeBinding
import com.turkcell.payment_tracking_app.model.PaymentType
import com.turkcell.payment_tracking_app.model.Period

class MainActivity : AppCompatActivity() {
    lateinit var binding : ActivityMainBinding
    var paymentTypeList = ArrayList<PaymentType>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val x = PaymentTypeOperation(this)
        val y = x.getPaymentTypes()
        println(y)
        paymentTypeList = x.getPaymentTypes()

        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.rwPaymentTypes.layoutManager = layoutManager
        binding.rwPaymentTypes.adapter = PaymentTypeAdapter(this, paymentTypeList, ::itemClick,::addButtonClick)

        binding.btnAddPaymentType.setOnClickListener {
            val intent = Intent(this, PaymentTypeActivity::class.java)
            //intent.putExtra("paymentType",paymentTypeList[]) -> gonderirken kullanılacak
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
                val paymentType = gelenData!!.getSerializableExtra("paymentType") as PaymentType

                if (paymentType.id == null) {
                    paymentType.id = paymentTypeList.size
                    paymentTypeList.add(paymentType)
                } else {
                    val existingPaymentType = paymentTypeList.filter { it.id == paymentType.id }.first()
                    existingPaymentType.title = paymentType.title
                    existingPaymentType.period = paymentType.period
                    existingPaymentType.periodDay = paymentType.periodDay
                    existingPaymentType.payments = paymentType.payments
                }

                binding.rwPaymentTypes.adapter?.notifyDataSetChanged()


                val x = PaymentTypeOperation(this)
                val y = x.getPaymentTypes()
                println(y)
            }
        }

    var resultLauncher2 =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_CANCELED) {
                    Toast.makeText(this, "No Changes!", Toast.LENGTH_SHORT).show()
                }
                if (result.resultCode == RESULT_OK) {
                    Toast.makeText(this, "Changes Saved Successfully!", Toast.LENGTH_SHORT).show()
                    val gelenData: Intent? = result.data
                    val paymentType = gelenData!!.getSerializableExtra("paymentType") as PaymentType
                    val existingPaymentType = paymentTypeList.filter { it.id == paymentType.id }.first()
                    existingPaymentType.payments = paymentType.payments
                    println(paymentType.payments)
                    println("x")

                }
            }

    private fun itemClick(position: Int) {
        val intent = Intent(this, PaymentTypeDetailsActivity::class.java)
        intent.putExtra("paymentType", paymentTypeList.get(position))
        resultLauncher.launch(intent)
    }

    private fun addButtonClick(position: Int){
        //customer!!.addProductToBasket((categories[categoryPos]).products[position])
        //binding.twBasketPrice.text = customer!!.getTotalPrice()
        val intent = Intent(this, PaymentActivity::class.java)
        intent.putExtra("paymentType", paymentTypeList.get(position))
        resultLauncher2.launch(intent)
    }
}