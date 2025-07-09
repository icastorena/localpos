package com.pds.localpos.common.filter

import com.pds.localpos.common.util.CorrelationIdHolder
import jakarta.servlet.Filter
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import org.slf4j.MDC
import org.springframework.stereotype.Component
import java.io.IOException
import java.util.*

@Component
class CorrelationIdFilter : Filter {

    companion object {
        private const val HEADER_NAME = "X-Correlation-ID"
    }

    @Throws(IOException::class, jakarta.servlet.ServletException::class)
    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        try {
            val correlationId = (request as HttpServletRequest).getHeader(HEADER_NAME)
                ?.takeIf { it.isNotBlank() }
                ?: UUID.randomUUID().toString()

            CorrelationIdHolder.set(correlationId)
            MDC.put("correlationId", correlationId)
            chain.doFilter(request, response)
        } finally {
            CorrelationIdHolder.clear()
            MDC.clear()
        }
    }
}
