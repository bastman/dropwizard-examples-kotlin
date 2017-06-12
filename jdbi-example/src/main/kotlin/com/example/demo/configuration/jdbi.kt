package com.example.demo.configuration

import io.dropwizard.Configuration
import io.dropwizard.jdbi.DBIFactory
import io.dropwizard.setup.Environment
import org.skife.jdbi.v2.DBI
import ru.vyarus.guicey.jdbi.JdbiBundle
import ru.vyarus.guicey.jdbi.dbi.ConfigAwareProvider


fun createApplicationJdbiProvider(fn: (configuration: AppConfiguration, environment: Environment) -> DBI):ConfigAwareProvider<DBI, AppConfiguration> {
    val dbiProvider: ConfigAwareProvider<DBI, AppConfiguration> = object : ConfigAwareProvider<DBI, AppConfiguration> {
        override fun get(configuration: AppConfiguration, environment: Environment): DBI {
            return fn(configuration, environment)
            /*
            val jdbi = DBIFactory().build(environment, configuration.database, "database")

            //jdbi.installPlugin(KotlinPlugin())
            //jdbi.installPlugin(KotlinSqlObjectPlugin());
            //jdbi.registerArgumentFactory(InstantAsSqlTimestampArgument())
            // jdbi.onDemand()
            return jdbi
            */
        }
    }

    return dbiProvider
}

fun <C : Configuration> createApplicationJdbiBundle(dbi: ConfigAwareProvider<DBI, C>): JdbiBundle {
    return JdbiBundle.forDbi(dbi)
}