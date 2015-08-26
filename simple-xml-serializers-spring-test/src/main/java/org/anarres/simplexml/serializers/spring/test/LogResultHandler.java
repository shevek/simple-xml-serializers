package org.anarres.simplexml.serializers.spring.test;

import javax.annotation.Nonnull;
// import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.web.servlet.result.PrintingResultHandler;
import org.springframework.util.CollectionUtils;

/**
 *
 * @author shevek
 */
public class LogResultHandler extends PrintingResultHandler {

    @Nonnull
    public static LogResultHandler print() {
        return new LogResultHandler();
    }

    private static final Logger LOG = LoggerFactory.getLogger(LogResultHandler.class);

    public LogResultHandler() {
        super(new LogValuePrinter());
    }

    @Override
    protected void printResolvedException(Exception resolvedException) throws Exception {
        super.printResolvedException(resolvedException);
        if (resolvedException != null) {
            LOG.info("Stack trace", resolvedException);
        }
    }

    private static class LogValuePrinter implements ResultValuePrinter {

        @Override
        public void printHeading(String heading) {
            LOG.info(String.format("%20s:", heading));
        }

        @Override
        public void printValue(String label, Object value) {
            if (value != null && value.getClass().isArray())
                value = CollectionUtils.arrayToList(value);
            String text = String.valueOf(value);
            // if (!StringUtils.isAsciiPrintable(text)) text = "<unprintable data>";
            LOG.info(String.format("%20s = %s", label, text));
        }
    }

}
