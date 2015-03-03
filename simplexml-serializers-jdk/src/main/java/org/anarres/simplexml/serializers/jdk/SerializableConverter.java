/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.simplexml.serializers.jdk;

import com.google.auto.service.AutoService;
import com.google.common.collect.Maps;
import java.io.Serializable;
import java.util.Collections;
import java.util.Map.Entry;
import javax.annotation.Nonnull;
import org.anarres.simplexml.serializers.common.ConverterFactory;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.convert.Converter;
import org.simpleframework.xml.stream.InputNode;
import org.simpleframework.xml.stream.OutputNode;

/**
 *
 * @author shevek
 */
@AutoService(ConverterFactory.class)
public class SerializableConverter extends AbstractConverter implements Converter<Serializable>, ConverterFactory {

    public static class Inner {

        private static final SerializableConverter INSTANCE = new SerializableConverter();
    }

    @Nonnull
    public static SerializableConverter getInstance() {
        return Inner.INSTANCE;
    }
    public static final String ATTRIBUTE = "data";

    @Override
    public Iterable<? extends Entry<? extends Class<?>, ? extends Converter<?>>> newConverters(Serializer serializer) {
        return Collections.singleton(Maps.immutableEntry(Serializable.class, this));
    }

    @Override
    public Serializable read(InputNode node) throws Exception {
        // This could be getAttribute() except for @ElementMap inlining data values.
        String data = getAttributeOrValue(node, ATTRIBUTE);
        return SerializableTransform.toObject(data);
    }

    @Override
    public void write(OutputNode node, Serializable value) throws Exception {
        String data = SerializableTransform.toString(value);
        node.setAttribute(ATTRIBUTE, data);
    }
}
