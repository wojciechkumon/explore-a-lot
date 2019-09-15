package com.hackyeah.lotapi

const val SECRET_KEY = "2przp49a52"
const val X_API_KEY = "9YFNNKS31u9gCFKPetPWdAAjEXnED0B3K18AeYgg"
const val API_URL_BASE = "https://api.lot.com/flights-dev/v2"

data class LotCredentials(
    val accessToken: String,
    val xApiKey: String
)
