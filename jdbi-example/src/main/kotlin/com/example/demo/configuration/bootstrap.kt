package com.example.demo.configuration

import com.example.demo.logging.AppLogger
import io.dropwizard.lifecycle.Managed

class AppBootstrap : Managed {
    private val LOGGER = AppLogger(this::class.java)
    override fun start() {
        LOGGER.info("start()")
    }

    override fun stop() {
        LOGGER.info("stop()")
    }
}
