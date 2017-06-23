package com.example.demo.configuration.properties

import com.example.demo.bundles.typesafeconfig.TypesafeConfigurationBundle
import com.example.demo.configuration.AppConfiguration
import io.dropwizard.configuration.ResourceConfigurationSourceProvider
import io.dropwizard.setup.Bootstrap

object ConfigurationPropertiesBundleProvider {

    fun createBundle():TypesafeConfigurationBundle<AppConfiguration> {
        return TypesafeConfigurationBundle<AppConfiguration>(
            // nodeName within .conf file
            configName = "app"
        )
    }

    fun bootstrap(bootstrap: Bootstrap<AppConfiguration>) {
        // use classpath resources (rather than files)
        bootstrap.configurationSourceProvider = ResourceConfigurationSourceProvider()
        bootstrap.addBundle(createBundle())
    }
}