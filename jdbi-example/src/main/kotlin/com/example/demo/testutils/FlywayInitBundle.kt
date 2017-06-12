package com.example.demo.testutils

import com.example.demo.configuration.AppConfiguration
import io.dropwizard.db.DataSourceFactory
import io.dropwizard.lifecycle.Managed
import org.flywaydb.core.Flyway
import ru.vyarus.dropwizard.guice.module.installer.bundle.GuiceyBootstrap
import ru.vyarus.dropwizard.guice.module.installer.bundle.GuiceyBundle
import javax.inject.Inject

// see: https://github.com/xvik/dropwizard-guicey-examples/blob/master/jdbi/src/test/groovy/ru/vyarus/dropwizard/guice/examples/util/FlywayInitBundle.groovy
class FlywayInitBundle : GuiceyBundle {

    override fun initialize(bootstrap: GuiceyBootstrap) {
        bootstrap.extensions(FlywaySupport::class.java)
    }
}

internal class FlywaySupport : Managed {
    @Inject
    lateinit var conf:AppConfiguration

    var flyway: Flyway?=null

    override fun start() {
        if (flyway != null) {
            return
        }
        flyway ={
            val flyway = Flyway();
            val f: DataSourceFactory = conf.database
            flyway.setDataSource(f.url, f.user, f.password);
            flyway.migrate();

            flyway
        } .invoke()
    }

    override fun stop() {
        flyway?.clean()
        flyway=null
    }

}