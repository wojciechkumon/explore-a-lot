package com.hackyeah.lotapi

import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

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

fun lotAvailability(lotCredentials: LotCredentials, input: LotAvailabilityInput): Mono<LotAvailabilityResponse> {
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

    return WebClient.create()
        .post()
        .uri(url)
        .syncBody(request)
        .header("X-Api-Key", lotCredentials.xApiKey)
        .header("Authorization", "Bearer ${lotCredentials.accessToken}")
        .header("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE)
        .retrieve()
        .onStatus({t -> t.is5xxServerError },{t -> t.bodyToMono(String::class.java).doOnEach{println(it)}.map { RuntimeException(it) } })
        .bodyToMono(LotAvailabilityResponse::class.java)
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

data class LotAvailabilityResponse(
    val data: List<List<Offer>>,
    val status: String,
    val errors: Any
)

data class Offer(
    val offerId: String,
    val totalPrice: TotalPrice,
    val outbound: Flight,
    val inbound: Flight,
    val url: String
)

data class TotalPrice(
    val price: Double,
    val basePrice: Double,
    val tax: Double,
    val currency: String
)

data class Flight(
    val duration: Double,
    val segments: List<Segment>,
    val fareType: String,
    val price: Double,
    val id: Int
)

data class Segment(
    val idInfoSegment: Int,
    val departureAirport: String,
    val arrivalAirport: String,
    val departureDate: String,
    val arrivalDate: String,
    val carrier: String,
    val flightNumber: String,
    val operationCarrier: String,
    val equipment: String,
    val duration: Double,
    val stopTime: Double,
    val scheduleChange: Double
)
