package com.hackyeah.lotapi

import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.web.client.RestTemplate
import java.util.concurrent.atomic.AtomicReference
import org.springframework.http.HttpMethod.POST
import org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE


private val lotCredentials: AtomicReference<LotCredentials> = AtomicReference()

private data class TokenRequest(
    val secret_key: String
)

data class TokenResponse(
    val access_token: String,
    val expires_in: Int,
    val token_type: String
)

//fun getLotCredentials(): LotCredentials = lotCredentials.get() ?: newCredentials()

fun newCredentials(): LotCredentials {
    val newToken = requestNewToken()
    val newCredentials = LotCredentials(accessToken = newToken, xApiKey = X_API_KEY)
    lotCredentials.set(newCredentials)
    return newCredentials
}

private fun requestNewToken(): String {
    val url = "$API_URL_BASE/auth/token/get"
    val headers = HttpHeaders()
    headers["X-Api-Key"] = X_API_KEY
    headers["Content-Type"] = APPLICATION_JSON_UTF8_VALUE
    val body = HttpEntity(TokenRequest(SECRET_KEY), headers)

    val response = RestTemplate()
        .exchange(url, POST, body, TokenResponse::class.java)
    return response.body.access_token
}
