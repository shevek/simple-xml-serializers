/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.simplexml.serializers.jdk;

import com.google.auto.service.AutoService;
import com.google.common.collect.Maps;
import java.util.Collections;
import java.util.Map.Entry;
import java.util.UUID;
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
public class UUIDConverter extends AbstractConverter implements Converter<UUID>, ConverterFactory {

    public static final String ATTRIBUTE = "text";

    @Override
    @SuppressWarnings("unchecked")  // Generic array creation.
    public Iterable<? extends Entry<? extends Class<?>, ? extends Converter<?>>> newConverters(Serializer serializer) {
        return Collections.singletonList(Maps.immutableEntry(UUID.class, this));
    }

    @Override
    public UUID read(InputNode node) throws Exception {
        String text = getAttribute(node, ATTRIBUTE);
        return UUID.fromString(text);
    }

    @Override
    public void write(OutputNode node, UUID value) throws Exception {
        node.setAttribute(ATTRIBUTE, String.valueOf(value));
    }
}
