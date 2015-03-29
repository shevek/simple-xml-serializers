/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.simplexml.serializers.spring.remote;

import java.io.InvalidClassException;
import java.net.ConnectException;
import javax.annotation.Nonnull;
import org.anarres.simplexml.factory.PersisterFactory;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.simpleframework.xml.Serializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.remoting.RemoteAccessException;
import org.springframework.remoting.RemoteConnectFailureException;
import org.springframework.remoting.RemoteInvocationFailureException;
import org.springframework.remoting.support.UrlBasedRemoteAccessor;
import org.springframework.util.ReflectionUtils;

/**
 *
 * @author shevek
 */
public class SimpleXmlInvokerClientInterceptor extends UrlBasedRemoteAccessor
        implements SimpleXmlInvokerClientConfiguration, MethodInterceptor {

    private Serializer serializer = PersisterFactory.newDefaultPersister();
    private SimpleXmlInvokerRequestExecutor simpleXmlInvokerRequestExecutor;

    @Override
    public Serializer getSerializer() {
        return serializer;
    }

    @Autowired(required = false)
    public void setSerializer(@Nonnull Serializer serializer) {
        this.serializer = serializer;
    }

    public void setSimpleXmlInvokerRequestExecutor(SimpleXmlInvokerRequestExecutor simpleXmlInvokerRequestExecutor) {
        this.simpleXmlInvokerRequestExecutor = simpleXmlInvokerRequestExecutor;
    }

    @Nonnull
    public SimpleXmlInvokerRequestExecutor getSimpleXmlInvokerRequestExecutor() {
        if (simpleXmlInvokerRequestExecutor == null)
            simpleXmlInvokerRequestExecutor = new SimpleXmlInvokerRequestExecutor();
        return simpleXmlInvokerRequestExecutor;
    }

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        if (ReflectionUtils.isToStringMethod(methodInvocation.getMethod())) {
            return "HTTP invoker proxy for service URL [" + getServiceUrl() + "]";
        }

        SimpleXmlInvocation invocation = new SimpleXmlInvocation(methodInvocation);
        SimpleXmlInvocationResult result = null;
        try {
            result = executeRequest(invocation, methodInvocation);
        } catch (Throwable ex) {
            throw convertSimpleXmlInvokerAccessException(ex);
        }
        try {
            return result.get();
        } catch (Throwable ex) {
            if (result.hasInvocationTargetException()) {
                throw ex;
            } else {
                throw new RemoteInvocationFailureException("Invocation of method [" + methodInvocation.getMethod()
                        + "] failed in HTTP invoker remote service at [" + getServiceUrl() + "]", ex);
            }
        }
    }

    @Nonnull
    protected SimpleXmlInvocationResult executeRequest(SimpleXmlInvocation invocation, MethodInvocation originalInvocation) throws Exception {
        return executeRequest(invocation);
    }

    @Nonnull
    protected SimpleXmlInvocationResult executeRequest(SimpleXmlInvocation invocation) throws Exception {
        return getSimpleXmlInvokerRequestExecutor().executeRequest(this, invocation);
    }

    protected RemoteAccessException convertSimpleXmlInvokerAccessException(Throwable ex) {
        if (ex instanceof ConnectException) {
            throw new RemoteConnectFailureException(
                    "Could not connect to HTTP invoker remote service at [" + getServiceUrl() + "]", ex);
        } else if (ex instanceof ClassNotFoundException || ex instanceof NoClassDefFoundError || ex instanceof InvalidClassException) {
            throw new RemoteAccessException(
                    "Could not deserialize result from HTTP invoker remote service [" + getServiceUrl() + "]", ex);
        } else {
            throw new RemoteAccessException(
                    "Could not access HTTP invoker remote service at [" + getServiceUrl() + "]", ex);
        }
    }
}
