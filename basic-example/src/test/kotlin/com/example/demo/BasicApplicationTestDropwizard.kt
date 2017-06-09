package com.example.demo

import com.example.demo.configuration.AppConfiguration
import com.example.demo.configuration.ConfigFacebook
import io.dropwizard.client.JerseyClientBuilder
import io.dropwizard.testing.ResourceHelpers
import io.dropwizard.testing.junit.DropwizardAppRule
import org.assertj.core.api.Assertions.assertThat
import org.junit.ClassRule
import org.junit.Test
import ru.vyarus.dropwizard.guice.injector.lookup.InjectorLookup


class BasicApplicationTestDropwizard {

    companion object {
        @ClassRule @JvmField
        val RULE = DropwizardAppRule<AppConfiguration>(
            BasicApplication::class.java,
            ResourceHelpers.resourceFilePath("example-config.yml")
        )
    }


    @Test
    fun testFoo() {
        val client = JerseyClientBuilder(RULE.environment).build("test client")

        val response = client.target(
            String.format("http://localhost:%d/config/describe", RULE.localPort))
            .request()
            .get()

        val entity = response.readEntity(Any::class.java)

        assertThat(response.getStatus()).isEqualTo(200)

        val injector = InjectorLookup.getInjector(RULE.getApplication()).get()
        val fb = injector.getInstance(ConfigFacebook::class.java)

        println(fb)
    }
}