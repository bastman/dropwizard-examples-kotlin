package com.example.demo.logging.appender

import ch.qos.logback.classic.LoggerContext
import ch.qos.logback.classic.filter.ThresholdFilter
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.contrib.jackson.JacksonJsonFormatter
import ch.qos.logback.contrib.json.classic.JsonLayout
import ch.qos.logback.core.Appender
import ch.qos.logback.core.ConsoleAppender
import ch.qos.logback.core.encoder.LayoutWrappingEncoder
import com.fasterxml.jackson.annotation.JsonTypeName
import io.dropwizard.logging.AbstractAppenderFactory
import io.dropwizard.logging.async.AsyncAppenderFactory
import io.dropwizard.logging.filter.LevelFilterFactory
import io.dropwizard.logging.layout.LayoutFactory
import java.time.ZoneOffset
import java.util.TimeZone

@JsonTypeName("console-json")
data class ConsoleJsonAppenderFactory(
        private val appenderName: String = "console-json-appender",
        private val timestampFormat: String = "YYYY-MM-dd'T'HH:mm:ssZ"
) : AbstractAppenderFactory<ILoggingEvent>() {

    private val target: ConsoleStream = ConsoleStream.STDOUT
    private val appendLineSeparator: Boolean = true

    companion object {
        private val jsonFormatter = JacksonJsonFormatter()
    }

    init {
        timeZone = TimeZone.getTimeZone(ZoneOffset.UTC)
    }

    enum class ConsoleStream(val value: String) {
        STDOUT("System.out"),
        STDERR("System.err");
    }

    override fun build(
            loggerContext: LoggerContext,
            applicationName: String,
            layoutFactory: LayoutFactory<ILoggingEvent>,
            levelFilterFactory: LevelFilterFactory<ILoggingEvent>,
            asyncAppenderFactory: AsyncAppenderFactory<ILoggingEvent>
    ): Appender<ILoggingEvent> {

        val me = this

        val jsonLayout = JsonLayout().apply {
            context = loggerContext
            isAppendLineSeparator = appendLineSeparator
            timestampFormat = me.timestampFormat
            timestampFormatTimezoneId = timeZone.id
            jsonFormatter = Companion.jsonFormatter
        }

        val jsonLayoutWrappingEncoder = LayoutWrappingEncoder<ILoggingEvent>().apply {
            context = loggerContext
            layout = jsonLayout
        }

        val filter = ThresholdFilter().apply {
            setLevel(threshold.toString())

            start()
        }

        val appender = ConsoleAppender<ILoggingEvent>().apply {
            name = appenderName
            context = loggerContext
            encoder = jsonLayoutWrappingEncoder
            target = me.target.value

            addFilter(filter)
            start()
        }

        return wrapAsync(appender, asyncAppenderFactory, loggerContext)
    }
}
