package com.example.demo

import com.example.demo.configuration.ConfigFacebook
import com.example.demo.configuration.ConfigGoogle
import com.example.demo.configuration.ConfigPaypal
import com.example.demo.resources.ConfigResource
import com.nhaarman.mockito_kotlin.spy
import org.amshove.kluent.`should be instance of`
import org.amshove.kluent.`should contain`
import org.amshove.kluent.`should equal`
import org.amshove.kluent.shouldBeInstanceOf
import org.junit.Before
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.MockitoAnnotations
import javax.inject.Inject


class BasicApplicationTestMockito {

    @Inject
    lateinit var configPaypal: ConfigPaypal
    @Inject
    lateinit var configGoogle: ConfigGoogle
    @Inject
    lateinit var configFacebook: ConfigFacebook

    @InjectMocks
    lateinit var resource: ConfigResource

    @Before
    fun setup() {
        configFacebook = spy(ConfigFacebook(apiKey = "facebook-apikey-spy"))
        configPaypal = spy(ConfigPaypal(apiKey = "paypal-apikey-spy"))
        configGoogle = spy(ConfigGoogle(apiKey = "google-apikey-spy"))

        MockitoAnnotations.initMocks(this)
        resource shouldBeInstanceOf ConfigResource::class
    }


    @Test
    fun `configFacebook is stubbed`() {
        configFacebook `should be instance of` ConfigFacebook::class
        configFacebook.apiKey `should contain` "facebook"
        configFacebook.apiKey `should contain` "spy"
    }


    @Test
    fun `resource returns valid response`() {
        val response = resource.describe()

        response.status `should equal` 200

        val entity = response.entity as ConfigResource.ConfigResourceDescribeResponse

        entity.configFacebook.apiKey `should contain` "facebook"
        entity.configFacebook.apiKey `should contain` "spy"
        entity.configGoogle.apiKey `should contain` "google"
        entity.configGoogle.apiKey `should contain` "spy"
        entity.configPaypal.apiKey `should contain` "paypal"
        entity.configPaypal.apiKey `should contain` "spy"
    }
}