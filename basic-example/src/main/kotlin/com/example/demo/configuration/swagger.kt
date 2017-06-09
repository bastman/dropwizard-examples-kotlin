package com.example.demo.configuration

import io.federecio.dropwizard.swagger.SwaggerBundle
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration

fun createApplicationSwaggerBundle(fn: (configuration: AppConfiguration) -> SwaggerBundleConfiguration): SwaggerBundle<AppConfiguration> {
    return object : SwaggerBundle<AppConfiguration>() {
        override fun getSwaggerBundleConfiguration(configuration: AppConfiguration): SwaggerBundleConfiguration {
            //return configuration.swaggerBundleConfiguration
            return fn(configuration)
        }
    }
}
