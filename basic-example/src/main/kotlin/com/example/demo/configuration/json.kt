package com.example.demo.configuration

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule

fun createApplicationJsonObjectMapper(): ObjectMapper {
    return jacksonObjectMapper()
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
}
