package org.anarres.simplexml.serializers.spring.remote;

import java.io.Writer;
import org.slf4j.Logger;
import org.springframework.util.Assert;

/**
 *
 * @author shevek
 */
public class Slf4jLogWriter extends Writer {

    private final Logger logger;

    private final StringBuilder buffer = new StringBuilder();

    /**
     * Create a new CommonsLogWriter for the given Commons Logging logger.
     * @param logger the Commons Logging logger to write to
     */
    public Slf4jLogWriter(Logger logger) {
        Assert.notNull(logger, "Logger must not be null");
        this.logger = logger;
    }

    public void write(char ch) {
        if (ch == '\n' && this.buffer.length() > 0) {
            this.logger.debug(this.buffer.toString());
            this.buffer.setLength(0);
        } else {
            this.buffer.append(ch);
        }
    }

    @Override
    public void write(char[] buffer, int offset, int length) {
        for (int i = 0; i < length; i++) {
            char ch = buffer[offset + i];
            if (ch == '\n' && this.buffer.length() > 0) {
                this.logger.debug(this.buffer.toString());
                this.buffer.setLength(0);
            } else {
                this.buffer.append(ch);
            }
        }
    }

    @Override
    public void flush() {
    }

    @Override
    public void close() {
    }

}
