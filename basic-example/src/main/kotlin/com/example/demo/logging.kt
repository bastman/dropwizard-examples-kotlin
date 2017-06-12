package com.example.demo

/*
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.contrib.jackson.JacksonJsonFormatter;
import ch.qos.logback.contrib.json.classic.JsonLayout;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.Layout;
import ch.qos.logback.ext.loggly.LogglyAppender;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.common.base.Optional;
import com.google.common.net.HostAndPort;
import io.dropwizard.logging.AbstractAppenderFactory;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.valuehandling.UnwrapValidatedValue;

import javax.validation.constraints.NotNull;



@JsonTypeName("loggly")
class LogglyAppenderFactory : AbstractAppenderFactory<*>() {

    @NotNull
    @get:JsonProperty
    @set:JsonProperty
    var server = HostAndPort.fromString("logs-01.loggly.com")

    @NotEmpty
    @get:JsonProperty
    @set:JsonProperty
    var token: String? = null

    @NotNull
    @UnwrapValidatedValue(false)
    @get:JsonProperty
    @set:JsonProperty
    var tag = Optional.absent<String>()

    fun build(context: LoggerContext, applicationName: String, layout: Layout<ILoggingEvent>?): Appender<ILoggingEvent> {
        val appender = LogglyAppender()

        val tagName = tag.or(applicationName)

        appender.setName("loggly-appender")
        appender.setContext(context)
        appender.setEndpointUrl(String.format(ENDPOINT_URL_TEMPLATE, server, token, tagName))
        appender.setLayout(layout ?: buildLayout(context))

        addThresholdFilter(appender, threshold)
        appender.start()

        return wrapAsync(appender)
    }

    protected fun buildLayout(context: LoggerContext): Layout<ILoggingEvent> {
        val formatter = JsonLayout()
        formatter.setJsonFormatter(JacksonJsonFormatter())
        formatter.setAppendLineSeparator(true)
        formatter.setContext(context)
        formatter.setTimestampFormat(ISO_8601_FORMAT)  //as per https://www.loggly.com/docs/automated-parsing/#json
        formatter.setTimestampFormatTimezoneId("UTC")
        formatter.start()
        return formatter
    }

    companion object {

        private val ISO_8601_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"

        private val ENDPOINT_URL_TEMPLATE = "https://%s/inputs/%s/tag/%s"
    }
}
*/
/*
import ch.qos.logback.classic.LoggerContext
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.contrib.json.JsonLayoutBase
import ch.qos.logback.contrib.json.classic.JsonLayout
import ch.qos.logback.core.Appender
import ch.qos.logback.core.ConsoleAppender
import ch.qos.logback.core.Layout
import ch.qos.logback.core.encoder.LayoutWrappingEncoder
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonTypeName
import io.dropwizard.logging.AbstractAppenderFactory

import javax.validation.constraints.NotNull
import java.util.TimeZone
import ch.qos.logback.contrib.jackson.JacksonJsonFormatter
import ch.qos.logback.core.spi.DeferredProcessingAware


/**
 * Log Json to the console.

 * @author jenshadlich@googlemail.com
 */
@JsonTypeName("console-json")
class ConsoleJsonAppenderFactory : AbstractAppenderFactory<DeferredProcessingAware>() {

    @get:JsonProperty
    @set:JsonProperty
    var name = "console-json-appender"
    @get:JsonProperty
    @set:JsonProperty
    var includeContextName = true

    override fun build(context: LoggerContext, applicationName: String, layoutFactory: Nothing, levelFilterFactory: Nothing, asyncAppenderFactory: Nothing): Appender<DeferredProcessingAware> {
        val layout = JsonLayout()
        layout.jsonFormatter = JacksonJsonFormatter()
        layout.isAppendLineSeparator = true
        layout.timestampFormatTimezoneId = "UTC"
        layout.isIncludeContextName = includeContextName

        val layoutEncoder = LayoutWrappingEncoder<ILoggingEvent>()
        layoutEncoder.setLayout(layout)

        val appender = ConsoleAppender<ILoggingEvent>()
        appender.name = name
        appender.context = context
        appender.setEncoder(layoutEncoder)
        addThresholdFilter(appender, threshold)
        appender.start()

        return wrapAsync(appender)
    }



}
    */

