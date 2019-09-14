package com.hackyeah.rest

import com.hackyeah.lotapi.CabinClass.ECONOMY
import com.hackyeah.lotapi.LotAvailabilityInput
import com.hackyeah.lotapi.Market.PL
import com.hackyeah.lotapi.Offer
import com.hackyeah.lotapi.TripType.RoundTrip
import com.hackyeah.lotapi.lotAvailability
import com.hackyeah.lotapi.newLotCredentials
import com.hackyeah.service.LotLuckyService
import com.hackyeah.service.LotTagsService
import com.hackyeah.service.TagsInput
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate
import java.time.format.DateTimeFormatter.ISO_DATE

@RestController
class LotApiRestController(
    private val lotLuckyService: LotLuckyService,
    private val lotTagsService: LotTagsService
) {

    @GetMapping("/api/dupa")
    fun test(): String {
        return newLotCredentials().accessToken
    }

    @GetMapping("/api/lotAvail")
    fun getLotAvail() {
        val lotCredentials = newLotCredentials()
        println(
            lotAvailability(
                lotCredentials,
                LotAvailabilityInput(
                    origin = "WAW",
                    destination = "AMS",
                    departureDate = "22102019",
                    returnDate = "25102019",
                    cabinClass = ECONOMY,
                    market = PL,
                    tripType = RoundTrip,
                    numberOfAdults = 1
                )
            ).block()
        )
    }

    @GetMapping("/api/lot/lucky/flights")
    fun getLuckyFlights(@RequestParam origin: String, @RequestParam numberOfAdults: Int): List<Offer> {
        println(origin)
        println(numberOfAdults)

        return lotLuckyService.getLuckyFlights(origin, numberOfAdults)
    }

    @GetMapping("/api/lot/tags/flights")
    fun getFlightsByTags(
        @RequestParam origin: String, @RequestParam numberOfAdults: Int,
        @RequestParam departureDateStart: String,
        @RequestParam departureDateEnd: String,
        @RequestParam tripDurationDays: Int, @RequestParam maxPricePerPerson: Double, @RequestParam tags: List<String>
    ): List<Offer> {
        println(origin)
        println(numberOfAdults)
        println(departureDateStart)
        println(departureDateEnd)
        println(tripDurationDays)
        println(maxPricePerPerson)
        println(tags)

        val departureStart = ISO_DATE.parse(departureDateStart, LocalDate::from)
        val departureEnd = ISO_DATE.parse(departureDateEnd, LocalDate::from)

        return lotTagsService.getMatchingFlights(
            TagsInput(
                origin = origin,
                numberOfAdults = numberOfAdults,
                departureDateStart = departureStart,
                departureDateEnd = departureEnd,
                tripDurationDays = tripDurationDays,
                maxPricePerPerson = maxPricePerPerson,
                tags = tags
            )
        )
    }
}
