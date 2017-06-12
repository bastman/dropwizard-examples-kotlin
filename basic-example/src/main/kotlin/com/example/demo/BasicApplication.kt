package com.example.demo

import com.example.demo.configuration.AppConfiguration
import com.example.demo.configuration.AppModule
import com.example.demo.configuration.configureApplicationJsonObjectMapper
import com.example.demo.configuration.createApplicationSwaggerBundle
import com.example.demo.logging.logger
import com.google.inject.Stage
import io.dropwizard.Application
import io.dropwizard.setup.Bootstrap
import io.dropwizard.setup.Environment
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import ru.vyarus.dropwizard.guice.GuiceBundle
import ru.vyarus.dropwizard.guice.injector.lookup.InjectorLookup
import ru.vyarus.guice.validator.ImplicitValidationModule
import java.time.Duration
import java.util.concurrent.TimeUnit

import net.logstash.logback.encoder.LogstashEncoder
import ch.qos.logback.core.encoder.LayoutWrappingEncoder
import ch.qos.logback.contrib.jackson.JacksonJsonFormatter
import ch.qos.logback.contrib.json.classic.JsonLayout
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
        configureApplicationJsonObjectMapper(bootstrap.objectMapper)

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

                .modules(
                    // bean validation
                    ImplicitValidationModule(), // ????
                    AppModule()
                )

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
                .build(Stage.PRODUCTION)
    }
}