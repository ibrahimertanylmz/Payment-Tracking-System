package com.turkcell.payment_tracking_app.view

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.turkcell.payment_tracking_app.adapter.PaymentTypeAdapter
import com.turkcell.payment_tracking_app.interactor.PaymentTrackingInteractor
import com.turkcell.payment_tracking_app.databinding.ActivityMainBinding
import com.turkcell.payment_tracking_app.model.PaymentType
import com.turkcell.payment_tracking_app.presenter.PaymentTrackingPresenter
import com.turkcell.payment_tracking_app.presenter.PaymentTrackingPresenterImpl

class MainActivity : AppCompatActivity() {
    lateinit var binding : ActivityMainBinding
    internal lateinit var ptPresenter: PaymentTrackingPresenter
    var paymentTypeList = ArrayList<PaymentType>()
    val ptInteractor = PaymentTrackingInteractor(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializeViews()

        binding.btnAddPaymentType.setOnClickListener {
            addPaymentTypeButtonClick()
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "No Changes!", Toast.LENGTH_SHORT).show()
            }
            if (result.resultCode == RESULT_OK) {
                Toast.makeText(this, "Changes Saved Successfully!", Toast.LENGTH_SHORT).show()
                /*val gelenData: Intent? = result.data
                val paymentType = gelenData!!.getSerializableExtra("paymentType") as PaymentType
                val isDeleted = gelenData!!.getBooleanExtra("isDeleted",false)

                if (paymentType.id == null) {
                    //paymentType.id = currentId
                    //currentId++
                    //paymentTypeList.add(paymentType)

                }else if(isDeleted){

                    paymentTypeList.removeIf {
                        it.id == paymentType.id
                    }

                    ptInteractor.deletePaymentType(paymentType.id!!)
                }
                else {
                    val existingPaymentType = paymentTypeList.filter { it.id == paymentType.id }.first()
                    existingPaymentType.title = paymentType.title
                    existingPaymentType.period = paymentType.period
                    existingPaymentType.periodDay = paymentType.periodDay
                    existingPaymentType.payments = paymentType.payments
                }*/

                //yukarısını sor (getPaymentTypes olayı)

                paymentTypeList = ptPresenter.getPaymentTypes()
                binding.rwPaymentTypes.adapter = PaymentTypeAdapter(this, paymentTypeList, ::itemClick,::addButtonClick)

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

    private fun initializeViews(){
        ptPresenter = PaymentTrackingPresenterImpl(this)
        ptPresenter.onAttach()
        paymentTypeList = ptPresenter.getPaymentTypes()
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.rwPaymentTypes.layoutManager = layoutManager
        binding.rwPaymentTypes.adapter = PaymentTypeAdapter(this, paymentTypeList, ::itemClick,::addButtonClick)
    }

    private fun addPaymentTypeButtonClick(){
        val intent = Intent(this, PaymentTypeActivity::class.java)
        resultLauncher.launch(intent)
    }

    private fun itemClick(position: Int) {
        val intent = Intent(this, PaymentTypeDetailsActivity::class.java)
        intent.putExtra("paymentType", paymentTypeList.get(position))

        if(paymentTypeList.get(position).id!= null){ // SOR!! bu şekilde mi başta mı çekilmeli
        paymentTypeList.get(position).payments = ptInteractor.getPaymentsWithId(paymentTypeList.get(position).id)
        }
        resultLauncher.launch(intent)
    }

    private fun addButtonClick(position: Int){
        val intent = Intent(this, PaymentActivity::class.java)
        intent.putExtra("paymentType", paymentTypeList.get(position))
        resultLauncher2.launch(intent)
    }
}