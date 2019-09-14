package com.hackyeah.lotapi

import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.web.client.RestTemplate

data class LotAvailabilityInput(
    val origin: String,
    val destination: String,
    val departureDate: String,
    val returnDate: String,
    val cabinClass: CabinClass,
    val market: Market,
    val tripType: TripType,
    val numberOfAdults: Int
)

enum class CabinClass(val code: String) {
    ECONOMY("E"),
    BUSINESS("B"),
    FIRST("F")
}

enum class Market(val code: String) {
    PL("PL")
}

enum class TripType(val code: String) {
    OneWay("O"),
    RoundTrip("R")
}

fun lotAvailability(lotCredentials: LotCredentials, input: LotAvailabilityInput) {
    val url = "$API_URL_BASE/booking/availability"
    val headers = HttpHeaders()
    headers["X-Api-Key"] = lotCredentials.xApiKey
    headers["Authorization"] = "Bearer ${lotCredentials.accessToken}"
    headers["Content-Type"] = MediaType.APPLICATION_JSON_UTF8_VALUE
    val request = LotAvailabilityRequest(
        LotAvailabilityParams(
            origin = listOf(input.origin),
            destination = listOf(input.destination),
            departureDate = listOf(input.departureDate),
            returnDate = input.returnDate,
            cabinClass = input.cabinClass.code,
            market = input.market.code,
            tripType = input.tripType.code,
            adt = input.numberOfAdults
        )
    )
    val body = HttpEntity(request, headers)

    val response = RestTemplate()
        .exchange(url, HttpMethod.POST, body, LotAvailabilityResponse::class.java)
    println(response)
}

private data class LotAvailabilityRequest(
    val params: LotAvailabilityParams
)

private data class LotAvailabilityParams(
    val origin: List<String>,
    val destination: List<String>,
    val departureDate: List<String>,
    val returnDate: String,
    val cabinClass: String,
    val market: String,
    val tripType: String,
    val adt: Int
)

private data class LotAvailabilityResponse(
    val data: List<Any>,
    val status: String,
    val errors: Any
)

private data class LotAvailabilityData(
    val x: String
)
