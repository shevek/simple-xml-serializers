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
import org.joda.time.LocalDateTime;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.convert.Converter;
import org.simpleframework.xml.stream.InputNode;
import org.simpleframework.xml.stream.OutputNode;

/**
 *
 * @author shevek
 */
@AutoService(ConverterFactory.class)
public class LocalDateTimeConverter extends AbstractConverter implements Converter<LocalDateTime>, ConverterFactory {

    public static final String ATTRIBUTE = "datetime";

    @Override
    public Iterable<? extends Map.Entry<? extends Class<?>, ? extends Converter<?>>> newConverters(Serializer serializer) {
        return Collections.singleton(Maps.immutableEntry(LocalDateTime.class, this));
    }

    @Override
    public LocalDateTime read(InputNode node) throws Exception {
        String text = getAttribute(node, ATTRIBUTE);
        return LocalDateTime.parse(text);
    }

    @Override
    public void write(OutputNode node, LocalDateTime value) throws Exception {
        String text = value.toString();
        node.setAttribute(ATTRIBUTE, text);
    }
}
