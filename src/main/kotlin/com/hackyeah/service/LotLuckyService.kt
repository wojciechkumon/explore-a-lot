package com.hackyeah.service

import com.hackyeah.lotapi.CabinClass
import com.hackyeah.lotapi.LotAvailabilityInput
import com.hackyeah.lotapi.Market
import com.hackyeah.lotapi.TripType
import com.hackyeah.lotapi.newLotCredentials
import com.hackyeah.lotapi.lotAvailability
import org.springframework.stereotype.Service

@Service
class LotLuckyService {

    fun getLuckyFlights(origin: String, numberOfAdults: Int) {
        val lotCredentials = newLotCredentials()

        val destination: String = "AMS"
        val departureDate: String = "22102019"
        val returnDate: String = "25102019"

        lotAvailability(
            lotCredentials, LotAvailabilityInput(
                origin = origin,
                destination = "AMS",
                departureDate = "22102019",
                returnDate = "25102019",
                cabinClass = CabinClass.ECONOMY,
                market = Market.PL,
                tripType = TripType.RoundTrip,
                numberOfAdults = numberOfAdults
            )
        )
    }
}