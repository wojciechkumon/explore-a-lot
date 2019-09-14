package com.hackyeah.lotapi

import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.web.client.RestTemplate
import java.util.concurrent.atomic.AtomicReference
import org.springframework.http.HttpMethod.POST
import org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE

const val SECRET_KEY = "2przp49a52"
const val X_API_KEY = "9YFNNKS31u9gCFKPetPWdAAjEXnED0B3K18AeYgg"
const val API_URL_BASE = "https://api.lot.com/flights-dev/v2"

private val lotCredentials: AtomicReference<LotCredentials> = AtomicReference()

data class LotCredentials(
    val accessToken: String,
    val xApiKey: String
)

private data class TokenRequest(
    val secret_key: String
)

data class TokenResponse(
    val access_token: String,
    val expires_in: Int,
    val token_type: String
)

fun getLotToken(): LotCredentials {
    val cachedCredentials: LotCredentials? = lotCredentials.get()
    return if (cachedCredentials != null) {
        cachedCredentials
    } else {
        val newToken = requestNewToken()
        val newCredentials = LotCredentials(accessToken = newToken, xApiKey = X_API_KEY)
        lotCredentials.set(newCredentials)
        return newCredentials
    }
}

fun requestNewToken(): String {
    val url = "$API_URL_BASE/auth/token/get"
    val headers = HttpHeaders()
    headers["X-Api-Key"] = X_API_KEY
    headers["Content-Type"] = APPLICATION_JSON_UTF8_VALUE
    val body = HttpEntity(TokenRequest(SECRET_KEY), headers)

    val response = RestTemplate()
        .exchange(url, POST, body, TokenResponse::class.java)
    return response.body.access_token
}


