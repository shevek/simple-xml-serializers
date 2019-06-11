/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.simplexml.serializers.jodatime;

import com.google.auto.service.AutoService;
import com.google.common.collect.Maps;
import java.util.Collections;
import java.util.Map;
import org.anarres.simplexml.serializers.common.ConverterFactory;
import org.anarres.simplexml.serializers.jdk.AbstractConverter;
import org.joda.time.Instant;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.convert.Converter;
import org.simpleframework.xml.stream.InputNode;
import org.simpleframework.xml.stream.OutputNode;

/**
 *
 * @author shevek
 */
@AutoService(ConverterFactory.class)
public class InstantConverter extends AbstractConverter implements Converter<Instant>, ConverterFactory {

    public static final String ATTRIBUTE = "instant";

    @Override
    public Iterable<? extends Map.Entry<? extends Class<?>, ? extends Converter<?>>> newConverters(Serializer serializer) {
        return Collections.singleton(Maps.immutableEntry(Instant.class, this));
    }

    @Override
    public Instant read(InputNode node) throws Exception {
        String text = getAttribute(node, ATTRIBUTE);
        return InstantTransform.parse(text);
    }

    @Override
    public void write(OutputNode node, Instant value) throws Exception {
        String text = value.toString();
        node.setAttribute(ATTRIBUTE, text);
    }
}
