package com.example.demo

import com.example.demo.configuration.*
import com.example.demo.resources.ConfigResource
import com.google.inject.Guice
import com.google.inject.Provides
import com.google.inject.util.Modules
import org.amshove.kluent.`should be instance of`
import org.amshove.kluent.`should contain`
import org.amshove.kluent.`should equal`
import org.junit.Test
import ru.vyarus.dropwizard.guice.module.support.DropwizardAwareModule
import javax.inject.Singleton


class BasicApplicationTestGuice {

    internal class TestModule() : DropwizardAwareModule<AppConfiguration>() {
        override fun configure() {}
        @Provides @Singleton
        fun provideConfigFacebook(): ConfigFacebook {
            return ConfigFacebook(apiKey = "facebook-apikey-mocked")
        }

        @Provides @Singleton
        fun provideConfigGoogle(): ConfigGoogle {
            return ConfigGoogle(apiKey = "google-apikey-mocked")
        }

        @Provides @Singleton
        fun provideConfigPaypal(): ConfigPaypal {
            return ConfigPaypal(apiKey = "paypal-apikey-mocked")
        }
    }

    val injector = Guice.createInjector(
            // the default module
            Modules.override(AppModule())
                    // will be modified by TestModule
                    .with(TestModule()
                    )
    )
    val resource: ConfigResource = injector.getInstance(
            ConfigResource::class.java
    )

    @Test
    fun `injector returns ConfigFacebook`() {
        val fb = injector.getInstance(ConfigFacebook::class.java)

        fb `should be instance of` ConfigFacebook::class
        fb.apiKey `should contain` "facebook"
    }

    @Test
    fun `resource returns valid response`() {
        val response = resource.describe()

        response.status `should equal` 200

        val entity = response.entity as ConfigResource.ConfigResourceDescribeResponse

        entity.configFacebook.apiKey `should contain` "facebook"
        entity.configFacebook.apiKey `should contain` "mocked"
        entity.configGoogle.apiKey `should contain` "google"
        entity.configGoogle.apiKey `should contain` "mocked"
        entity.configPaypal.apiKey `should contain` "paypal"
        entity.configPaypal.apiKey `should contain` "mocked"
    }
}