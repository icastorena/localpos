package com.pds.localpos.userservice.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "app.cors")
class CorsProperties {
    lateinit var allowedOrigins: List<String>
}
