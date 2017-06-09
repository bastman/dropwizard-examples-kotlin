package com.example.demo.configuration

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.google.inject.Inject
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration
import org.hibernate.validator.constraints.NotEmpty
import javax.validation.Valid
import javax.validation.constraints.NotNull


@JsonIgnoreProperties(ignoreUnknown = true)
data class AppConfiguration @Inject constructor(
        @JsonProperty("swagger")
        val swaggerBundleConfiguration: SwaggerBundleConfiguration,
        @JsonProperty("paypal")
        val paypal: ConfigPaypal,
        @JsonProperty("google")
        val google: ConfigGoogle,
    @JsonProperty("facebook")
    @Valid @NotNull
    val facebook: ConfigFacebook
) : io.dropwizard.Configuration() {
}

data class ConfigPaypal @Inject constructor(
        val apiKey: String
)

data class ConfigGoogle @Inject constructor(
        val apiKey: String
)
data class ConfigFacebook(
    @Valid @NotEmpty val apiKey: String
)

