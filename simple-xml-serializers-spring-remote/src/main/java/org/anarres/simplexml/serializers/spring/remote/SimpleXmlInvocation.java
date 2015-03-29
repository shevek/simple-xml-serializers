/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.simplexml.serializers.spring.remote;

import com.google.common.annotations.VisibleForTesting;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import org.anarres.simplexml.factory.PersisterUtils;
import org.aopalliance.intercept.MethodInvocation;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementArray;
import org.simpleframework.xml.ElementMap;
import org.simpleframework.xml.Root;
import org.springframework.remoting.support.RemoteInvocation;

/**
 *
 * @author shevek
 */
@Root(name = "call")
public class SimpleXmlInvocation extends RemoteInvocation {

    @VisibleForTesting
    /* pp */ static final Object[] EMPTY_OBJECT_ARRAY = new Object[0];
    @VisibleForTesting
    /* pp */ static final Class<?>[] EMPTY_CLASS_ARRAY = new Class<?>[0];

    @Nonnull
    private static Class<?>[] unwrap(@CheckForNull Class<?>[] params) {
        if (params == null)
            return EMPTY_CLASS_ARRAY;
        return params;
    }

    @Nonnull
    private static Object[] unwrap(@CheckForNull Object[] args) {
        if (args == null)
            return EMPTY_OBJECT_ARRAY;
        for (int i = 0; i < args.length; i++)
            args[i] = PersisterUtils.unwrap(args[i]);
        return args;
    }

    public SimpleXmlInvocation(
            @Nonnull @Attribute(name = "method") String methodName,
            @Nonnull @ElementArray(name = "params", entry = "param") Class<?>[] parameterTypes,
            @Nonnull @ElementArray(name = "args", entry = "arg") Object[] arguments) {
        super(methodName, unwrap(parameterTypes), unwrap(arguments));
    }

    public SimpleXmlInvocation(MethodInvocation methodInvocation) {
        super(methodInvocation);
    }

    @Override
    @Nonnull
    @Attribute(name = "method")
    public String getMethodName() {
        return super.getMethodName();
    }

    @Override
    @Nonnull
    @ElementArray(name = "params", entry = "param", required = false, empty = false)
    public Class<?>[] getParameterTypes() {
        return super.getParameterTypes();
    }

    @Nonnull
    @ElementArray(name = "args", entry = "arg", required = false, empty = false)
    public Object[] getArgumentsWrapped() {
        Object[] args = super.getArguments();
        args = Arrays.copyOf(args, args.length);
        for (int i = 0; i < args.length; i++)
            args[i] = PersisterUtils.wrap(args[i]);
        return args;
    }

    /** For serialization only. */
    @ElementMap(name = "attrs", key = "key", value = "value", attribute = true, required = false)
    public Map<String, ? extends Object> getAttributeMap() {
        return super.getAttributes();
    }

    /** For serialization only. */
    @ElementMap(name = "attrs", key = "key", value = "value", attribute = true, required = false)
    public void setAttributeMap(Map<String, ? extends Object> attributes) {
        Iterator<? extends Map.Entry<String, ?>> it = attributes.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, ? extends Object> e = it.next();
            if (e.getValue() != null)
                if (!(e.getValue() instanceof Serializable))
                    throw new IllegalArgumentException("Not serializable: " + e.getValue().getClass());
        }
        super.setAttributes((Map<String, Serializable>) attributes);
    }
}
