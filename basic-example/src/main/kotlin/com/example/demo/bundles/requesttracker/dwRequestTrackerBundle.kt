package com.example.demo.bundles.requesttracker

// https://github.com/service-enabled/dropwizard-request-tracker/tree/master/src/main/java/com/serviceenabled/dropwizardrequesttracker
// If you'd like the ID to be output in your logs, add %X{correlationId} to your logFormat.

import io.dropwizard.ConfiguredBundle
import io.dropwizard.setup.Bootstrap
import io.dropwizard.setup.Environment
import org.slf4j.MDC
import java.util.UUID
import java.util.EnumSet
import javax.servlet.DispatcherType
import javax.servlet.Filter
import javax.servlet.FilterChain
import javax.servlet.FilterConfig
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.ws.rs.client.ClientRequestContext
import javax.ws.rs.client.ClientRequestFilter

data class RequestTrackerConfiguration(
        val headerName: String = "X-CORRELATION-ID",
        val mdcKey: String = "correlationId",
        val addResponseHeader: Boolean = false,
        val readRequestHeader: Boolean = false,
        val correlationIdSupplier: RequestCorrelationIdSupplier =
        RequestTrackerConfiguration.defaultCorrelationIdSupplier()
) {
    companion object {
        fun defaultCorrelationIdSupplier(): RequestCorrelationIdSupplier = {
            UUID.randomUUID().toString()
        }
    }

}

typealias RequestCorrelationIdSupplier = () -> String

abstract class RequestTrackerBundle<T>() : ConfiguredBundle<T> {

    override fun run(configuration: T, environment: Environment) {
        val requestTrackerConfiguration = this.getRequestTrackerConfiguration(
                configuration
        )
        environment.servlets()
                .addFilter(
                        "request-tracker-servlet-filter",
                        RequestTrackerServletFilter(
                                configuration = requestTrackerConfiguration
                        )
                )
                .addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), false, "*")
    }

    override fun initialize(bootstrap: Bootstrap<*>) {}
    abstract fun getRequestTrackerConfiguration(configuration: T): RequestTrackerConfiguration
}

class RequestTrackerServletFilter(
        private val configuration: RequestTrackerConfiguration) : Filter {

    override fun init(filterConfig: FilterConfig) {}

    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        val httpServletRequest = request as HttpServletRequest
        val httpServletResponse = response as HttpServletResponse

        val providedCorrelationId: String? = if (configuration.readRequestHeader) {
            httpServletRequest.getHeader(
                    configuration.headerName
            )
        } else {
            null
        }

        val correlationId = if (providedCorrelationId.isNullOrBlank()) {
            configuration.correlationIdSupplier()
        } else {
            providedCorrelationId
        }

        MDC.put(configuration.mdcKey, correlationId)

        if (configuration.addResponseHeader) {
            httpServletResponse.addHeader(configuration.headerName, correlationId)
        }

        chain.doFilter(request, response)
    }

    override fun destroy() {}
}

class RequestTrackerClientFilter(
        private val configuration: RequestTrackerConfiguration) : ClientRequestFilter {

    override fun filter(clientRequest: ClientRequestContext) {
        val correlationId = MDC.get(configuration.mdcKey) ?: configuration.correlationIdSupplier()
        // and mdc.put ?????

        clientRequest.headers.add(configuration.headerName, correlationId)
    }
}
