/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.simplexml.serializers.jdk;

import com.google.auto.service.AutoService;
import com.google.common.collect.Maps;
import java.util.Collections;
import java.util.Map;
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
public class ShortArrayConverter extends AbstractConverter implements Converter<short[]>, ConverterFactory {

    public static final String ATTRIBUTE = "data";

    @Override
    public Iterable<? extends Map.Entry<? extends Class<?>, ? extends Converter<?>>> newConverters(Serializer serializer) {
        return Collections.singleton(Maps.immutableEntry(short[].class, this));
    }

    @Override
    public short[] read(InputNode node) throws Exception {
        String data = getAttributeOrValue(node, ATTRIBUTE);
        return ShortArrayTransform.toObject(data);
    }

    @Override
    public void write(OutputNode node, short[] value) throws Exception {
        String data = ShortArrayTransform.toString(value);
        node.setAttribute(ATTRIBUTE, data);
    }
}
