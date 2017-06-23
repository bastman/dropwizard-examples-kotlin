package com.example.demo.configuration.swagger

import com.example.demo.configuration.AppConfiguration
import io.dropwizard.setup.Bootstrap
import io.federecio.dropwizard.swagger.SwaggerBundle
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration

object SwaggerBundleProvider {

    fun createBundle() = object : SwaggerBundle<AppConfiguration>() {
        override fun getSwaggerBundleConfiguration(
                configuration: AppConfiguration
        ) = configuration.swagger
    }

    fun bootstrap(bootstrap: Bootstrap<AppConfiguration>) {
        bootstrap.addBundle(createBundle())
    }
}