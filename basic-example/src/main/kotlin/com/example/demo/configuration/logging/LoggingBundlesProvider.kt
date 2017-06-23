package com.example.demo.configuration.logging

import com.example.demo.bundles.requesttracker.RequestTrackerBundle
import com.example.demo.bundles.requesttracker.RequestTrackerConfiguration
import com.example.demo.configuration.AppConfiguration
import io.dropwizard.setup.Bootstrap

object LoggingBundlesProvider {

    fun bootstrap(bootstrap: Bootstrap<AppConfiguration>) {
        val requestLoggerBundle = createRequestTrackerBundle()
        bootstrap.addBundle(requestLoggerBundle)
    }

    fun createRequestTrackerBundle(): RequestTrackerBundle<AppConfiguration> {
        val bundleConfiguration = RequestTrackerConfiguration(
                addResponseHeader = true,
                readRequestHeader = false
        )

        return object : RequestTrackerBundle<AppConfiguration>() {
            override fun getRequestTrackerConfiguration(
                    configuration: AppConfiguration
            ): RequestTrackerConfiguration = bundleConfiguration
        }
    }
}