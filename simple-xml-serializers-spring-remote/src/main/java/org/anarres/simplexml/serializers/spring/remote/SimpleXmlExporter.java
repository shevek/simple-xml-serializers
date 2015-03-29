/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.simplexml.serializers.spring.remote;

import com.google.common.io.Closeables;
import java.io.InputStream;
import java.io.OutputStream;
import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import org.anarres.simplexml.factory.PersisterFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.simpleframework.xml.Serializer;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.remoting.support.RemoteExporter;
import org.springframework.util.Assert;

/**
 *
 * @author shevek
 */
public class SimpleXmlExporter extends RemoteExporter implements InitializingBean {

    private static final Logger LOG = LoggerFactory.getLogger(SimpleXmlExporter.class);
    @Nonnull
    private Serializer serializer = PersisterFactory.newDefaultPersister();
    @CheckForNull
    private Logger debugLogger = LOG;
    private boolean close = false;
    private Object target;

    @Autowired(required = false)
    public void setSerializer(@Nonnull Serializer serializer) {
        this.serializer = serializer;
    }

    public void setDebug(boolean debug) {
        this.debugLogger = (debug ? LOG : null);
    }

    public void setClose(boolean close) {
        this.close = close;
    }

    @Override
    public void afterPropertiesSet() {
        prepare();
    }

    /**
     * Initialize this exporter.
     */
    public void prepare() {
        target = getProxyForService();
        Assert.notNull(target, "Failed to create proxy target for " + getServiceInterface());
    }

    /**
     * Perform an invocation on the exported object.
     *
     * @param inputStream the request stream
     * @param outputStream the response stream
     * @throws Throwable if invocation failed
     */
    public void invoke(@Nonnull InputStream inputStream, @Nonnull OutputStream outputStream) throws Throwable {
        Assert.notNull(target, "No target object available.");
        Assert.notNull(target.getClass(), "Target object has no class.");

        ClassLoader originalClassLoader = overrideThreadContextClassLoader();

        try {
            /*
             Flushable debugFlushable = null;
             if (this.debugLogger != null && this.debugLogger.isDebugEnabled()) {
             Writer debugWriter = new Slf4jLogWriter(this.debugLogger);
             OutputStream debugOutput = new WriterOutputStream(debugWriter);
             inputStream = new TeeInputStream(inputStream, debugOutput, true);
             outputStream = new TeeOutputStream(outputStream, debugOutput);
             debugFlushable = debugOutput;
             }
             */

            SimpleXmlInvocation invocation = serializer.read(SimpleXmlInvocation.class, inputStream, false);
            /*
             if (debugFlushable != null)
             debugFlushable.flush();
             */
            SimpleXmlInvocationResult result = new SimpleXmlInvocationResult();
            try {
                Object value = invocation.invoke(target);
                if (LOG.isDebugEnabled())
                    LOG.debug("Remote invocation returned value: " + value);
                result.setValue(value);
            } catch (Throwable t) {
                if (LOG.isDebugEnabled())
                    LOG.debug("Remote invocation caught error: " + t, t);
                result.setThrowable(t);
            }
            if (LOG.isDebugEnabled())
                LOG.debug("Remote invocation result is " + result);
            serializer.write(result, outputStream);
            outputStream.write('\n');   // Makes debugging come out more nicely with CommonsLogWriter.
            outputStream.flush();
        } finally {
            resetThreadContextClassLoader(originalClassLoader);
            if (close) {
                Closeables.close(inputStream, true);
                Closeables.close(outputStream, true);
            }
        }
    }
}
