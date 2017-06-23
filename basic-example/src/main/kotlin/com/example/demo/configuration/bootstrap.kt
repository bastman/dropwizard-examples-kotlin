package com.example.demo.configuration

import com.example.demo.logging.logger
import io.dropwizard.lifecycle.Managed

class AppBootstrap : Managed {
    private val LOGGER by logger()

    override fun start() {
        LOGGER.info("start()")
    }

    override fun stop() {
        LOGGER.info("stop()")
    }
}
