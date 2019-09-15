package com.lot.android.api

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