package com.turkcell.payment_tracking_app.model

import java.io.Serializable

class PaymentType(var title : String) : Serializable {
    var id : Int? = null
    var period : Period? = null
    var periodDay : Int? = null
}
