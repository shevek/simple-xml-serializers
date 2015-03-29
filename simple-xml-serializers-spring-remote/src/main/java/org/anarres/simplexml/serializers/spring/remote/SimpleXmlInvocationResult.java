/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.simplexml.serializers.spring.remote;

import java.lang.reflect.InvocationTargetException;
import javax.annotation.CheckForNull;
import org.anarres.simplexml.factory.PersisterUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
import org.springframework.remoting.support.RemoteInvocationUtils;

/**
 *
 * @author shevek
 */
@Root(name = "result")
public class SimpleXmlInvocationResult {

    private static final Logger LOG = LoggerFactory.getLogger(SimpleXmlInvocationResult.class);
    @CheckForNull
    private Object value;
    @CheckForNull
    private Throwable throwable;

    @Element(name = "v", required = false)
    public Object getValueWrapped() {
        return PersisterUtils.wrap(getValue());
    }

    @Element(name = "v", required = false)
    public void setValueWrapped(@CheckForNull Object value) {
        setValue(PersisterUtils.unwrap(value));
    }

    @CheckForNull
    public Object getValue() {
        return value;
    }

    public void setValue(@CheckForNull Object value) {
        LOG.info("Set value to " + value);
        this.value = value;
    }

    @CheckForNull
    @Element(name = "t", required = false)
    public Throwable getThrowable() {
        return throwable;
    }

    @Element(name = "t", required = false)
    public void setThrowable(@CheckForNull Throwable throwable) {
        this.throwable = throwable;
    }

    public boolean hasInvocationTargetException() {
        return throwable instanceof InvocationTargetException;
    }

    @CheckForNull
    public Object get() throws Throwable {
        // LOG.info("value=" + value + "; throwable=" + throwable);
        Throwable t = this.throwable;
        if (t == null)
            return this.value;
        while (t instanceof InvocationTargetException)
            t = ((InvocationTargetException) t).getTargetException();
        RemoteInvocationUtils.fillInClientStackTraceIfPossible(t);
        throw t;
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder(getClass().getSimpleName()).append("(");
        if (throwable != null)
            buf.append("throwable=").append(throwable).append("; ");
        buf.append("value=").append(value);
        buf.append(")");
        return buf.toString();
    }
}
