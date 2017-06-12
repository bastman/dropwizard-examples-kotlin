/*
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.contrib.jackson.JacksonJsonFormatter;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.FileAppender;
import ch.qos.logback.core.Layout;
import ch.qos.logback.core.encoder.LayoutWrappingEncoder;
import com.cofi.api.disbursement.util.logging.JsonStacktraceLayout;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.dropwizard.logging.FileAppenderFactory;

@JsonTypeName("jsonfile")
public class JsonFileAppenderFactory extends FileAppenderFactory {

    private boolean prettyPrint = false;

    private boolean includeStackTrace = true;

    @JsonProperty
    public boolean isPrettyPrint() {
        return prettyPrint;
    }

    @JsonProperty
    public void setPrettyPrint(boolean prettyPrint) {
        this.prettyPrint = prettyPrint;
    }

    @JsonProperty
    public boolean isIncludeStackTrace() {
        return includeStackTrace;
    }

    @JsonProperty
    public void setIncludeStackTrace(boolean includeStackTrace) {
        this.includeStackTrace = includeStackTrace;
    }

    @Override
    public Appender<ILoggingEvent> build(LoggerContext context, String applicationName, Layout<ILoggingEvent> layout) {
        final FileAppender<ILoggingEvent> appender = buildAppender(context);
        appender.setName("Jsonfile-appender");

        appender.setAppend(true);
        appender.setContext(context);

        LayoutWrappingEncoder<ILoggingEvent> layoutEncoder = new LayoutWrappingEncoder<>();
        JsonStacktraceLayout jsonLayout = new JsonStacktraceLayout();
        jsonLayout.setContext(context);
        jsonLayout.setIncludeStacktrace(this.includeStackTrace);
        JacksonJsonFormatter jacksonJsonFormatter = new JacksonJsonFormatter();
        jacksonJsonFormatter.setPrettyPrint(this.prettyPrint);


        jsonLayout.setJsonFormatter(jacksonJsonFormatter);
        layoutEncoder.setLayout(jsonLayout);
        appender.setEncoder(layoutEncoder);

        appender.setPrudent(false);
        addThresholdFilter(appender, threshold);
        appender.stop();
        appender.start();

        return wrapAsync(appender);
    }

}
*/