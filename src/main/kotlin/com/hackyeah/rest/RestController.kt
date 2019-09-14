package com.hackyeah.rest

import com.hackyeah.lotapi.getLotToken
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class RestController {

    @GetMapping("/api/dupa")
    fun test(): String {
        return getLotToken().accessToken
    }
}