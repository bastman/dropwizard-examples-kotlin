package com.example.demo.configuration.json

import com.example.demo.configuration.AppConfiguration
import com.example.demo.logging.appender.ConsoleJsonAppenderFactory
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule
import io.dropwizard.setup.Bootstrap

object JacksonObjectMapperProvider {
    fun bootstrap(bootstrap: Bootstrap<AppConfiguration>) {
        bootstrap.objectMapper = configure(bootstrap.objectMapper)
    }

    fun configure(mapper: ObjectMapper): ObjectMapper {
        return mapper.copy()
                .registerModules(
                        KotlinModule(),
                        JavaTimeModule(),
                        Jdk8Module(),
                        ParameterNamesModule()
                )
                .disable(
                        SerializationFeature.WRITE_DATES_AS_TIMESTAMPS,
                        SerializationFeature.WRITE_DURATIONS_AS_TIMESTAMPS
                )
                .apply {
                    registerSubtypes(ConsoleJsonAppenderFactory::class.java)
                }
    }
}