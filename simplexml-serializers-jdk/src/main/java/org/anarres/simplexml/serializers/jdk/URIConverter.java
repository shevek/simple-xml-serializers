/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.simplexml.serializers.jdk;

import com.google.auto.service.AutoService;
import com.google.common.collect.Maps;
import java.net.URI;
import java.util.Collections;
import java.util.Map.Entry;
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
public class URIConverter extends AbstractConverter implements Converter<URI>, ConverterFactory {

    public static final String ATTRIBUTE = "text";

    @Override
    @SuppressWarnings("unchecked")  // Generic array creation.
    public Iterable<? extends Entry<? extends Class<?>, ? extends Converter<?>>> newConverters(Serializer serializer) {
        return Collections.singletonList(Maps.immutableEntry(URI.class, this));
    }

    @Override
    public URI read(InputNode node) throws Exception {
        String text = getAttribute(node, ATTRIBUTE);
        return new URI(text);
    }

    @Override
    public void write(OutputNode node, URI value) throws Exception {
        node.setAttribute(ATTRIBUTE, String.valueOf(value));
    }
}
