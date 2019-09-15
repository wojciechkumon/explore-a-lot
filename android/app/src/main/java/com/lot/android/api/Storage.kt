package com.lot.android.api

object Storage {
    var tags = ""
    var start_date_1 = ""
    var start_date_2 = ""
    var days = ""
    var adults = 0
    var teenagers = 0
    var children = 0
    var infants = 0
    var budget = 0

    fun clearStorage() {
        tags = ""
        start_date_1 = ""
        start_date_2 = ""
        days = ""
        adults = 0
        teenagers = 0
        children = 0
        infants = 0
        budget = 0
    }
}