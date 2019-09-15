package com.hackyeah.service

import com.hackyeah.lotapi.CabinClass
import com.hackyeah.lotapi.LotAvailabilityInput
import com.hackyeah.lotapi.LotAvailabilityResponse
import com.hackyeah.lotapi.Market
import com.hackyeah.lotapi.Offer
import com.hackyeah.lotapi.TripType
import com.hackyeah.lotapi.newLotCredentials
import com.hackyeah.lotapi.lotAvailability
import com.hackyeah.throttler.Throttler
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.core.publisher.toFlux
import java.time.LocalDate
import java.util.stream.Collectors.toList
import kotlin.random.Random

@Service
class LotLuckyService(
    private val throttler: Throttler
) {

    fun getLuckyFlights(
        origin: String,
        numberOfAdults: Int,
        numberOfTeenagers: Int,
        numberOfChildren: Int,
        numberOfInfants: Int
    ): List<Offer> {
        val lotCredentials = newLotCredentials()

        val destination = destinationTags.keys.shuffled().first()

        @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
        return getDepartureDates()
            .toFlux()
            .parallel()
            .flatMap {
                throttler.throttle(
                    lotAvailability(
                        lotCredentials, LotAvailabilityInput(
                            origin = origin,
                            destination = destination,
                            departureDate = it.format(lotDateFormatter),
                            returnDate = it.plusDays(7).format(lotDateFormatter),
                            cabinClass = CabinClass.ECONOMY,
                            market = Market.PL,
                            tripType = TripType.RoundTrip,
                            numberOfAdults = numberOfAdults,
                            numberOfTeenagers = numberOfTeenagers,
                            numberOfChildren = numberOfChildren,
                            numberOfInfants = numberOfInfants
                        )
                    ).onErrorResume { Mono.just(LotAvailabilityResponse(listOf(), "500", Any())) }
                )
            }
            .filter { it.data.isNotEmpty() }
            .flatMap { it.data.toFlux() }
            .flatMap { it.toFlux() }
            .sequential()
            .sort(Comparator.comparing { offer: Offer -> offer.totalPrice.price })
            .take(30)
            .collect(toList())
            .block()
    }

    private fun getDepartureDates(): List<LocalDate> {
        val departureDate = LocalDate.now()
            .plusDays(Random.nextInt(21, 42).toLong())
        val departures = arrayListOf(departureDate)
        for (x in 2 until 20 step 2) {
            departures.add(departureDate.plusDays(x.toLong()))
        }
        return departures
    }
}