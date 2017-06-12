package com.example.demo

import com.example.demo.configuration.AppConfiguration
import com.example.demo.configuration.ConfigFacebook
import com.example.demo.resources.ConfigResource
import io.dropwizard.testing.ResourceHelpers
import org.amshove.kluent.`should be instance of`
import org.amshove.kluent.`should contain`
import org.amshove.kluent.`should equal`
import org.junit.Rule
import org.junit.Test
import ru.vyarus.dropwizard.guice.injector.lookup.InjectorLookup
import ru.vyarus.dropwizard.guice.test.GuiceyAppRule

//@UseGuiceyApp(BasicApplication::class)
class BasicApplicationTestGuicey {

    // see: https://xvik.github.io/dropwizard-guicey/4.0.1/guide/test/

    @Rule @JvmField
    val RULE: GuiceyAppRule<AppConfiguration> = GuiceyAppRule<AppConfiguration>(
            BasicApplication::class.java,
            ResourceHelpers.resourceFilePath("test-config.yml")
    )

    @Test
    fun `rule returns ConfigFacebook`() {
        val fb = RULE.getBean(ConfigFacebook::class.java)

        fb `should be instance of` ConfigFacebook::class
        fb.apiKey `should contain` "facebook"
    }

    @Test
    fun `injector returns ConfigFacebook`() {
        val injector = InjectorLookup.getInjector(RULE.getApplication()).get()
        val fb = injector.getInstance(ConfigFacebook::class.java)

        fb `should be instance of` ConfigFacebook::class
        fb.apiKey `should contain` "facebook"
    }

    @Test
    fun `resource returns valid response`() {
        val resource = RULE.getBean(ConfigResource::class.java)
        val response = resource.describe()

        response.status `should equal` 200

        val entity = response.entity as ConfigResource.ConfigResourceDescribeResponse

        entity.configFacebook.apiKey `should contain` "facebook"
        entity.configGoogle.apiKey `should contain` "google"
        entity.configPaypal.apiKey `should contain` "paypal"
    }

}