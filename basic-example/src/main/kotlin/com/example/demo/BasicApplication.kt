package com.example.demo

import com.example.demo.configuration.AppConfiguration
import com.example.demo.configuration.AppModule
import com.example.demo.configuration.createApplicationSwaggerBundle
import com.example.demo.logging.logger
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule
import io.dropwizard.Application
import io.dropwizard.setup.Bootstrap
import io.dropwizard.setup.Environment
import ru.vyarus.dropwizard.guice.GuiceBundle
import ru.vyarus.dropwizard.guice.injector.lookup.InjectorLookup

fun main(args: Array<String>) {
    BasicApplication().run(*args)
}

class BasicApplication : Application<AppConfiguration>() {
    companion object {
        private val LOGGER by logger()
    }

    override fun initialize(bootstrap: Bootstrap<AppConfiguration>) {
        super.initialize(bootstrap)

        // must be first, to be able to parse json/yml config files !!!!
        //bootstrap.objectMapper = createApplicationJsonObjectMapper()


        // must be first, to be able to parse json/yml config files !!!!
        bootstrap.objectMapper
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




        bootstrap.addBundle(createApplicationSwaggerBundle { it.swaggerBundleConfiguration })
        bootstrap.addBundle(createGuiceBundle())
    }

    override fun run(configuration: AppConfiguration, environment: Environment) {
        val injector = InjectorLookup.getInjector(this).get()
        LOGGER.info("+++++++++++++++++++++++++")
        LOGGER.info("run() injector=$injector")
        LOGGER.info("+++++++++++++++++++++++++")

        // environment.lifecycle().manage(guiceBundle.injector.getInstance(TemplateHealthCheck::class.java))
    }

    private fun createGuiceBundle(): GuiceBundle<AppConfiguration> {
        return GuiceBundle.builder<AppConfiguration>()
                //.bundles(jdbiBundle)

                // enable classpath scanning
                .enableAutoConfig(javaClass.`package`.name)

                .modules(AppModule())

                // enable @WebServlet, @WebFilter, @WebListener
                .useWebInstallers()

                // enable DiagnosticBundle.class
                // limit output to only show installers
                //.printAvailableInstallers()

                // optional: enable automatic registration of dropwizard bundles
                // guicey checks registered dropwizard bundles if they implement interface GuiceyBundle and register them as guicey bundles
                // - e.g.: diagnostics bundle
                .configureFromDropwizardBundles()
                .printDiagnosticInfo()

                // optional: auto install dropwizard commands
                .searchCommands()

                // enable HkDebugBundle: strictScopeControl
                // throw ex if sth wrong with hk vs guice
                .strictScopeControl()

                .build()
    }
    /*
    private fun createSwaggerBundle(): SwaggerBundle<AppConfiguration> {
        return object : SwaggerBundle<AppConfiguration>() {
            override fun getSwaggerBundleConfiguration(configuration: AppConfiguration): SwaggerBundleConfiguration {
                return configuration.swaggerBundleConfiguration
            }
        }
    }
    */
}