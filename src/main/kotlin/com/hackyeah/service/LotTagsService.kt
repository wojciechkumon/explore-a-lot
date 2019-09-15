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
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.publisher.toFlux
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.stream.Stream
import kotlin.streams.toList

data class TagsInput(
    val origin: String,
    val numberOfAdults: Int,
    val numberOfTeenagers: Int,
    val numberOfChildren: Int,
    val numberOfInfants: Int,
    val departureDateStart: LocalDate,
    val departureDateEnd: LocalDate,
    val tripDurationDays: Int,
    val maxPricePerPerson: Double,
    val tags: List<String>
)

@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
@Service
class LotTagsService(
    private val throttler: Throttler
) {

    fun getMatchingFlights(input: TagsInput): List<Offer> {
        val lotCredentials = newLotCredentials()
        val destinations = findBestMatchingDestinationsByTags(input.tags)

        val datesToCheck = getRangeOfDates(input.departureDateStart, input.departureDateEnd).toList()
        val destinationDatesCombinations = destinations.stream()
            .flatMap { destination -> datesToCheck.stream().map { date -> DestinationDate(destination, date) } }
            .toList()
        val offers = Flux.fromIterable(destinationDatesCombinations)
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
                            numberOfAdults = input.numberOfAdults,
                            numberOfTeenagers = input.numberOfTeenagers,
                            numberOfChildren = input.numberOfChildren,
                            numberOfInfants = input.numberOfInfants
                        )
                    ).onErrorResume { Mono.just(LotAvailabilityResponse(listOf(), "500", Any())) }
                )
            }
            .filter { it.data.isNotEmpty() }
            .flatMap { it.data.toFlux() }
            .flatMap { it.toFlux() }
            .filter { it.totalPrice.price < input.maxPricePerPerson }
            .sequential()
            .distinct { it.offerId }
            .collectList()
            .block()

        val groupedOffers: Map<String, List<Offer>> = offers.asSequence()
            .groupBy { it.outbound.segments.last().arrivalAirport }
        val topOffers = groupedOffers.values
            .flatMap {
                it.sortedWith(Comparator.comparing { offer: Offer -> offer.totalPrice.price })
                    .take(10)
            }

        return topOffers
    }

    private fun findBestMatchingDestinationsByTags(tags: List<String>): List<String> {
        val destinationToMatchingTags = destinationTags.entries
            .map { entry -> Pair(entry.key, matchingTags(entry.value, tags)) }
        var max: Int = destinationToMatchingTags.map { it.second }.max()!!
        val destinations = arrayListOf<String>()
        destinations.addAll(takeDestinations(destinationToMatchingTags, max, MAX_RESULTS))
        while (destinations.size < MAX_RESULTS) {
            max -= 1
            destinations.addAll(takeDestinations(destinationToMatchingTags, max, MAX_RESULTS - destinations.size))
        }
        return destinations
    }

    private fun takeDestinations(
        destinationToMatchingTags: List<Pair<String, Int>>,
        max: Int?,
        amountToTake: Int
    ): List<String> =
        destinationToMatchingTags
            .filter { it.second == max }
            .map { it.first }
            .shuffled()
            .take(amountToTake)

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

private const val MAX_RESULTS = 3
val lotDateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("ddMMyyyy")

private data class DestinationDate(
    val destination: String,
    val date: String
)
