/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.simplexml.serializers.thrift;

import com.google.auto.service.AutoService;
import com.google.common.collect.Maps;
import java.util.Collections;
import java.util.Map;
import org.anarres.simplexml.serializers.common.ConverterFactory;
import org.anarres.simplexml.serializers.jdk.AbstractConverter;
import org.anarres.simplexml.serializers.jdk.ByteArrayTransform;
import org.apache.commons.lang3.ClassUtils;
import org.apache.thrift.TBase;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.convert.Converter;
import org.simpleframework.xml.stream.InputNode;
import org.simpleframework.xml.stream.OutputNode;

/**
 *
 * @author shevek
 */
@AutoService(ConverterFactory.class)
public class ThriftConverter extends AbstractConverter implements Converter<TBase<?, ?>>, ConverterFactory {

    public static final String TCLASS = "tclass";

    @Override
    public Iterable<? extends Map.Entry<? extends Class<?>, ? extends Converter<?>>> newConverters(Serializer serializer) {
        return Collections.singleton(Maps.immutableEntry(TBase.class, this));
    }

    @Override
    public TBase<?, ?> read(InputNode node) throws Exception {
        String typename = getAttribute(node, TCLASS);
        @java.lang.SuppressWarnings("rawtypes")
        Class<? extends TBase> type = ClassUtils.getClass(typename).asSubclass(TBase.class);
        String text = getValue(node);
        if (text == null)
            return null;
        byte[] data = ByteArrayTransform.toObject(text);
        if (data == null)
            return null;
        return ThriftUtils.load(type, data);
    }

    @Override
    public void write(OutputNode node, TBase<?, ?> value) throws Exception {
        byte[] data = ThriftUtils.save(value);
        String text = ByteArrayTransform.toString(data);
        node.setAttribute(TCLASS, value.getClass().getName());
        node.setValue(text);
    }
}
