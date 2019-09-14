package com.hackyeah.rest

import com.hackyeah.lotapi.CabinClass.ECONOMY
import com.hackyeah.lotapi.LotAvailabilityInput
import com.hackyeah.lotapi.Market.PL
import com.hackyeah.lotapi.TripType.RoundTrip
import com.hackyeah.lotapi.lotAvailability
import com.hackyeah.lotapi.newCredentials
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
        return newCredentials().accessToken
    }

    @GetMapping("/api/lotAvail")
    fun getLotAvail() {
        val lotCredentials = newCredentials()
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
        )
    }

    @GetMapping("/api/lot/lucky/flights")
    fun getLuckyFlights(@RequestParam origin: String, @RequestParam numberOfAdults: Int) {
        println(origin)
        println(numberOfAdults)

        lotLuckyService.getLuckyFlights(origin, numberOfAdults)
    }

    @GetMapping("/api/lot/tags/flights")
    fun getFlightsByTags(
        @RequestParam origin: String, @RequestParam numberOfAdults: Int,
        @RequestParam departureDateStart: String,
        @RequestParam departureDateEnd: String,
        @RequestParam tripDurationDays: Int, @RequestParam maxPricePerPerson: Double, @RequestParam tags: List<String>
    ) {
        println(origin)
        println(numberOfAdults)
        println(departureDateStart)
        println(departureDateEnd)
        println(tripDurationDays)
        println(maxPricePerPerson)
        println(tags)

        val departureStart = ISO_DATE.parse(departureDateStart, LocalDate::from)
        val departureEnd = ISO_DATE.parse(departureDateEnd, LocalDate::from)

        lotTagsService.getMatchingFlights(
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

// endpoint 1: feeling lucky WSZYSTKO - origin, numberOfAdults
// endpoint 2 (tagi): origin, numberOfAdults, czas start (przedzial), dlugosc pobytu, tagi, maxPricePerPerson

// tagi: List<String> // ?tags=tag1,tag2,tag3
