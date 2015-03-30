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
import org.anarres.simplexml.serializers.common.ConverterFactory;
import org.anarres.typeserializer.core.TypeSerializer;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.convert.Converter;
import org.simpleframework.xml.stream.InputNode;
import org.simpleframework.xml.stream.OutputNode;

/**
 *
 * @author shevek
 */
public class TypeConverter extends AbstractConverter implements Converter<Type>, ConverterFactory {

    public static class Inner {

        private static final TypeConverter INSTANCE = new TypeConverter();
    }

    @Nonnull
    public static TypeConverter getInstance() {
        return Inner.INSTANCE;
    }

    @Override
    public Iterable<? extends Map.Entry<? extends Class<?>, ? extends Converter<?>>> newConverters(Serializer serializer) {
        return Arrays.asList(
                Maps.immutableEntry(Type.class, this),
                Maps.immutableEntry(Class.class, this)
        );
    }
    public static final String ATTRIBUTE = "name";

    @Override
    public Type read(InputNode node) throws Exception {
        String name = getAttribute(node, ATTRIBUTE);
        return TypeSerializer.deserialize(name);
    }

    @Override
    public void write(OutputNode node, Type value) throws Exception {
        String name = TypeSerializer.serialize(value);
        node.setAttribute(ATTRIBUTE, name);
    }
}
