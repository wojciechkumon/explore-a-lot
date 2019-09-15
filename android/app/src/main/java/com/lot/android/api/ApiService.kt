package com.lot.android.api

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("tags/flights?origin=WAW")
    fun getFlights(
        @Query("numberOfAdults") adults: String,
        @Query("departureDateStart") dateStart1: String,
        @Query("departureDateEnd") dateStart2: String,
        @Query("tripDurationDays") duration: String,
        @Query("maxPricePerPerson") maxPrice: String,
        @Query("tags") tags: String,
        @Query("numberOfTeenagers") teenagers: String,
        @Query("numberOfChildren") children: String,
        @Query("numberOfInfants") infants: String
    ): Observable<List<Offer>>

    @GET("lucky/flights?origin=WAW")
    fun getLucky(
        @Query("numberOfAdults") adults: String,
        @Query("numberOfTeenagers") teenagers: String,
        @Query("numberOfChildren") children: String,
        @Query("numberOfInfants") infants: String
    ): Observable<List<Offer>>
}