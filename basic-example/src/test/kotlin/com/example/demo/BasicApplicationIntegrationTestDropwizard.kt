package com.example.demo

import com.example.demo.configuration.AppConfiguration
import com.example.demo.configuration.ConfigFacebook
import com.example.demo.resources.ConfigResource
import io.dropwizard.client.JerseyClientBuilder
import io.dropwizard.testing.ResourceHelpers
import io.dropwizard.testing.junit.DropwizardAppRule
import org.amshove.kluent.`should be instance of`
import org.amshove.kluent.`should contain`
import org.amshove.kluent.`should equal`
import org.junit.ClassRule
import org.junit.Test
import ru.vyarus.dropwizard.guice.injector.lookup.InjectorLookup


class BasicApplicationIntegrationTestDropwizard {

    companion object {
        @ClassRule @JvmField
        val RULE = DropwizardAppRule<AppConfiguration>(
                BasicApplication::class.java,
                ResourceHelpers.resourceFilePath("example-config.yml")
        )
    }

    @Test
    fun `injector returns ConfigFacebook`() {
        val injector = InjectorLookup.getInjector(RULE.getApplication()).get()
        val fb = injector.getInstance(ConfigFacebook::class.java)

        fb `should be instance of` ConfigFacebook::class
        fb.apiKey `should contain` "facebook"
    }

    @Test
    fun `api config describe works`() {
        val client = JerseyClientBuilder(RULE.environment).build("test client")

        val response = client.target(
                String.format("http://localhost:%d/config/describe", RULE.localPort))
                .request()
                .get()

        response.status `should equal` 200

        val entity = response.readEntity(ConfigResource.ConfigResourceDescribeResponse::class.java)

        val configFacebook = entity.configFacebook
        configFacebook.apiKey `should contain` "facebook"
    }
}