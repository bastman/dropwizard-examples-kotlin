package com.example.demo

import com.example.demo.configuration.AppConfiguration
import com.example.demo.configuration.ConfigFacebook
import com.example.demo.configuration.ConfigGoogle
import io.dropwizard.testing.ResourceHelpers
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
        //"src/test/resources/test-config.yml"
    )

    @Test
    fun testFoo() {
        println("foo")

        val fb = RULE.getBean(ConfigFacebook::class.java)
        //val fb = injector.getInstance(ConfigFacebook::class.java)
        val injector = InjectorLookup.getInjector(RULE.getApplication()).get()
        val google = injector.getInstance(ConfigGoogle::class.java)
        println("foo")
    }
}