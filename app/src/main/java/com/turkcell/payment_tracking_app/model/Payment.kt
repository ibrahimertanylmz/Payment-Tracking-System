package com.turkcell.payment_tracking_app.model

import java.io.Serializable

class Payment: Serializable {
    var id : Int? = null
    var date : String? = null
    var price : Int? = null
}