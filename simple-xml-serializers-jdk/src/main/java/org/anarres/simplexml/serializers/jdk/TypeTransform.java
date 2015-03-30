/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.simplexml.serializers.jdk;

import com.google.common.collect.Maps;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Map;
import javax.annotation.Nonnull;
import org.anarres.simplexml.serializers.common.TransformFactory;
import org.anarres.typeserializer.core.TypeSerializer;
import org.simpleframework.xml.transform.Transform;

/**
 *
 * @author shevek
 */
public class TypeTransform implements Transform<Type>, TransformFactory {

    public static class Inner {

        private static final TypeTransform INSTANCE = new TypeTransform();
    }

    @Nonnull
    public static TypeTransform getInstance() {
        return Inner.INSTANCE;
    }

    @Override
    public Iterable<? extends Map.Entry<? extends Class<?>, ? extends Transform<?>>> newTransforms() {
        return Arrays.asList(
                Maps.immutableEntry(Type.class, this),
                Maps.immutableEntry(Class.class, this)
        );
    }

    @Override
    public Type read(String value) throws Exception {
        return TypeSerializer.deserialize(TypeSerializer.getClassLoader(), value);
    }

    @Override
    public String write(Type value) throws Exception {
        return TypeSerializer.serialize(value);
    }
}
