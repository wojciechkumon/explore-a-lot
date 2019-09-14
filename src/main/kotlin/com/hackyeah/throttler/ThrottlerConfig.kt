package com.hackyeah.throttler

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class ThrottlerConfig {

    @Bean
    open fun throttler(): Throttler {
        return Throttler("LOT_API_THROTTLER", 6, 100)
    }
}
