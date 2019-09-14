package com.hackyeah.service

import org.springframework.stereotype.Service
import java.time.LocalDate

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
class LotTagsService {

    fun getMatchingFlights(input: TagsInput) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}