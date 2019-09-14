package com.hackyeah.service

import com.hackyeah.lotapi.CabinClass
import com.hackyeah.lotapi.LotAvailabilityInput
import com.hackyeah.lotapi.LotAvailabilityResponse
import com.hackyeah.lotapi.Market
import com.hackyeah.lotapi.TripType
import com.hackyeah.lotapi.newLotCredentials
import com.hackyeah.lotapi.lotAvailability
import com.hackyeah.throttler.Throttler
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.random.Random
import java.time.temporal.ChronoUnit
import java.util.stream.Stream
import kotlin.streams.toList

data class TagsInput(
    val origin: String,
    val numberOfAdults: Int,
    val departureDateStart: LocalDate,
    val departureDateEnd: LocalDate,
    val tripDurationDays: Int,
    val maxPricePerPerson: Double,
    val tags: List<String>
)

@Service
class LotTagsService(
    private val throttler: Throttler
) {

    fun getMatchingFlights(input: TagsInput) {
        val lotCredentials = newLotCredentials()
        input.maxPricePerPerson
        val destinations = findBestMatchingDestinationsByTags(input.tags)

        val datesToCheck = getRangeOfDates(input.departureDateStart, input.departureDateEnd).toList()
        val destinationDatesCombinations = destinations.stream()
            .flatMap { destination -> datesToCheck.stream().map { date -> DestinationDate(destination, date) } }
            .toList()

        val sum = Flux.fromIterable(destinationDatesCombinations)
            .parallel()
            .flatMap {
                throttler.throttle(
                    lotAvailability(
                        lotCredentials, LotAvailabilityInput(
                            origin = input.origin,
                            destination = it.destination,
                            departureDate = it.date,
                            returnDate = getReturnDate(it.date, input.tripDurationDays),
                            cabinClass = CabinClass.ECONOMY,
                            market = Market.PL,
                            tripType = TripType.RoundTrip,
                            numberOfAdults = Random.nextInt(8) + 1
                        )
                    ).onErrorResume { Mono.just(LotAvailabilityResponse(listOf(), "500", Any())) }
                )
            }
            .map { it.data.size }
            .sequential()
            .collectList()
            .block()
        println(0)
    }

    private fun findBestMatchingDestinationsByTags(tags: List<String>): List<String> {
        val destinationToMatchingTags = destinationTags.entries
            .map { entry -> Pair(entry.key, matchingTags(entry.value, tags)) }
        val max = destinationToMatchingTags.map { it.second }.max()
        // TODO fill to have always 3!!!
        return destinationToMatchingTags
            .filter { it.second == max }
            .map { it.first }
            .take(3)
    }

    private fun matchingTags(destinationTags: Set<String>, requiredTags: List<String>): Int =
        requiredTags.filter { destinationTags.contains(it) }.count()

    private fun getRangeOfDates(startDate: LocalDate, endDate: LocalDate): Stream<String> =
        Stream.iterate(startDate, { it.plusDays(1) })
            .limit(ChronoUnit.DAYS.between(startDate, endDate) + 1)
            .map { it.format(lotDateFormatter) }

    private fun getReturnDate(departureDate: String, tripDurationDays: Int): String {
        val parsedDate = LocalDate.parse(departureDate, lotDateFormatter)
        return parsedDate.plusDays(tripDurationDays.toLong()).format(lotDateFormatter)
    }
}

private val lotDateFormatter = DateTimeFormatter.ofPattern("ddMMyyyy")

private val destinationTags: Map<String, Set<String>> = mapOf(
    "AMS" to setOf("city", "modern", "sightseeing", "crowd"),
    "ARN" to setOf("city", "cold", "modern", "active", "sightseeing"),
    "BCN" to setOf("city", "hot", "sightseeing", "active", "crowd", "amusement"),
    "BEY" to setOf("city", "sightseeing", "ancient", "crowd", "oriental", "adventure"),
    "BLL" to setOf("city", "modern", "active", "crowd", "amusement", "adventure"),
    "CDG" to setOf("city", "modern", "ancient", "crowd", "contemplation", "sightseeing"),
    "CFU" to setOf("beach", "hot", "ancient", "lazy", "sightseeing", "loneliness", "mountains"),
    "CMB" to setOf("city", "oriental", "jungle", "active", "sightseeing", "crowd", "countryside"),
    "DBV" to setOf("beach", "forest", "mountains", "hot", "ancient", "lazy", "sightseeing", "city", "crowd"),
    "DEL" to setOf("city", "hot", "oriental", "active", "ancient", "crowd"),
    "DPS" to setOf("beach", "mountains", "lazy", "lake", "forest"),
    "GDN" to setOf("beach", "lazy", "sightseeing"),
    "GVA" to setOf("city", "mountains", "loneliness", "lake", "sightseeing", "contemplation"),
    "ICN" to setOf("city", "hot", "modern", "oriental", "sightseeing", "crowd"),
    "IST" to setOf("city", "hot", "ancient", "oriental", "sightseeing", "crowd"),
    "JFK" to setOf("city", "modern", "amusement", "sightseeing", "crowd", "active"),
    "KGD" to setOf("city", "ancient", "sightseeing", "crowd", "lazy", "contemplation"),
    "KRK" to setOf("city", "ancient", "contemplation", "sightseeing", "crowd", "active", "mountains"),
    "LAX" to setOf("city", "beach", "modern", "amusement", "crowd", "active", "hot"),
    "LWO" to setOf("city", "ancient", "lazy", "sightseeing", "crowd"),
    "MIA" to setOf("city", "modern", "amusement", "beach", "lazy"),
    "PEK" to setOf("city", "modern", "sightseeing", "crowd", "oriental", "contemplation"),
    "PRG" to setOf("city", "ancient", "modern", "contemplation", "sightseeing", "active"),
    "SKP" to setOf("city", "ancient", "mountains", "sightseeing", "active"),
    "SYD" to setOf("city", "modern", "active", "crowd", "beach"),
    "VIE" to setOf("city", "ancient", "contemplation", "sightseeing", "lazy"),
    "WAW" to setOf("city", "modern", "lazy", "crowd"),
    "YYZ" to setOf("city", "modern", "active", "cold", "lake", "countryside")
)

private data class DestinationDate(
    val destination: String,
    val date: String
)
