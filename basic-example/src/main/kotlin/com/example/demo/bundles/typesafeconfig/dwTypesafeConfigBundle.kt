package com.example.demo.bundles.typesafeconfig

/**
 * based on: https://github.com/mestevens/typesafe-dropwizard-configuration
 */

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory
import com.typesafe.config.ConfigValueFactory
import io.dropwizard.Bundle
import io.dropwizard.configuration.ConfigurationFactory
import io.dropwizard.configuration.ConfigurationFactoryFactory
import io.dropwizard.configuration.ConfigurationSourceProvider
import io.dropwizard.setup.Bootstrap
import io.dropwizard.setup.Environment
import javax.validation.Validator
import io.dropwizard.Configuration as DropwizardConfiguration

typealias TypesafeConfigProperties = Map<String, Any?>
typealias TypesafeConfigTransformer = (Config) -> Config

class TypesafeConfigurationBundle<T : DropwizardConfiguration>(
        private val configName: String?
) : Bundle {

    override fun initialize(bootstrap: Bootstrap<*>) {
        bootstrap.configurationFactoryFactory = TypesafeConfigurationFactoryFactory(bundle = this)
    }

    override fun run(environment: Environment) {}

    fun loadConfig(configurationSourceProvider: ConfigurationSourceProvider, path: String): Config {

        val config = configurationSourceProvider.open(path).use { inputStream ->
            val inputAsString = inputStream.bufferedReader().use { it.readText() }
            ConfigFactory.parseString(inputAsString)
        }

        return config
    }

    fun loadConfig(): Config {
        return ConfigFactory.load()
    }

    fun resolveConfig(config: Config): Config {
        val resolved = config.resolve()
        val subConfig = resolved.getConfig(configName)

        return subConfig
    }

    fun decodeAsDropwizardConfiguration(
            objectMapper: ObjectMapper,
            aClass: Class<T>,
            subConfigResolved: Config
    ): T {
        val unwrapped = subConfigResolved
                .root()
                .unwrapped()

        val objectMapperCustomized = objectMapper.copy()
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)
                .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)

        val dropwizardConfiguration = objectMapperCustomized.readValue(
                objectMapper.writeValueAsString(unwrapped), aClass
        )

        return dropwizardConfiguration
    }

    fun buildDropwizardConfiguration(
            config: Config,
            objectMapper: ObjectMapper,
            aClass: Class<T>
    ): T {
        val configResolved = resolveConfig(config = config)

        val dropwizardConfig = decodeAsDropwizardConfiguration(
                objectMapper = objectMapper,
                aClass = aClass,
                subConfigResolved = configResolved
        )

        return dropwizardConfig
    }
}

class TypesafeConfigurationFactoryFactory<T : DropwizardConfiguration>(
        private val bundle: TypesafeConfigurationBundle<T>
) : ConfigurationFactoryFactory<T> {

    override fun create(aClass: Class<T>,
                        validator: Validator,
                        objectMapper: ObjectMapper,
                        s: String): ConfigurationFactory<T> =
            TypesafeConfigurationFactory(
                    bundle = bundle,
                    objectMapper = objectMapper,
                    aClass = aClass
            )
}

class TypesafeConfigurationFactory<T : DropwizardConfiguration>(
        private val bundle: TypesafeConfigurationBundle<T>,
        private val objectMapper: ObjectMapper,
        private val aClass: Class<T>
) : ConfigurationFactory<T> {

    override fun build(configurationSourceProvider: ConfigurationSourceProvider, path: String): T {
        val config = bundle.loadConfig(
                configurationSourceProvider, path
        )

        return bundle.buildDropwizardConfiguration(
                config = config,
                objectMapper = objectMapper,
                aClass = aClass
        )
    }

    override fun build(): T {
        val config = bundle.loadConfig()

        return bundle.buildDropwizardConfiguration(
                config = config,
                objectMapper = objectMapper,
                aClass = aClass
        )
    }
}

fun Config.withProperties(properties: TypesafeConfigProperties?): Config {
    if (properties == null) {

        return this
    }
    var conf = this

    properties.forEach {
        conf = conf.withValue(it.key, ConfigValueFactory.fromAnyRef(it.value))
    }

    return conf
}

