/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.simplexml.serializers.dhcp4j;

import com.google.auto.service.AutoService;
import com.google.common.collect.Maps;
import java.util.Collections;
import java.util.Map;
import org.anarres.dhcp.common.address.InterfaceAddress;
import org.anarres.simplexml.serializers.common.ConverterFactory;
import org.anarres.simplexml.serializers.jdk.AbstractConverter;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.convert.Converter;
import org.simpleframework.xml.stream.InputNode;
import org.simpleframework.xml.stream.OutputNode;

/**
 *
 * @author shevek
 */
@AutoService(ConverterFactory.class)
public class InterfaceAddressConverter extends AbstractConverter implements Converter<InterfaceAddress>, ConverterFactory {

    public static final String ATTRIBUTE = "address";

    @Override
    public Iterable<? extends Map.Entry<? extends Class<?>, ? extends Converter<?>>> newConverters(Serializer serializer) {
        return Collections.singleton(Maps.immutableEntry(InterfaceAddress.class, this));
    }

    @Override
    public InterfaceAddress read(InputNode node) throws Exception {
        String addrtext = getAttribute(node, ATTRIBUTE);
        return InterfaceAddress.forString(addrtext);
    }

    @Override
    public void write(OutputNode node, InterfaceAddress value) throws Exception {
        String addrtext = value.toString();
        node.setAttribute(ATTRIBUTE, addrtext);
    }

}
