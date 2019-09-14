package com.hackyeah

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
open class LotApp

fun main(args: Array<String>) {
    runApplication<LotApp>(*args)
}
