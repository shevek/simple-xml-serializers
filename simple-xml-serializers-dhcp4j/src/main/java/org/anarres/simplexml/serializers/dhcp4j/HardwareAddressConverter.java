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
import org.anarres.simplexml.serializers.common.ConverterFactory;
import org.anarres.simplexml.serializers.jdk.AbstractConverter;
import org.apache.directory.server.dhcp.messages.HardwareAddress;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.convert.Converter;
import org.simpleframework.xml.stream.InputNode;
import org.simpleframework.xml.stream.OutputNode;

/**
 *
 * @author shevek
 */
@AutoService(ConverterFactory.class)
public class HardwareAddressConverter extends AbstractConverter implements Converter<HardwareAddress>, ConverterFactory {

    public static final String ATTRIBUTE = "address";

    @Override
    public Iterable<? extends Map.Entry<? extends Class<?>, ? extends Converter<?>>> newConverters(Serializer serializer) {
        return Collections.singleton(Maps.immutableEntry(HardwareAddress.class, this));
    }

    @Override
    public HardwareAddress read(InputNode node) throws Exception {
        String addrtext = getAttribute(node, ATTRIBUTE);
        return HardwareAddress.fromString(addrtext);
    }

    @Override
    public void write(OutputNode node, HardwareAddress value) throws Exception {
        String addrtext = value.getNativeRepresentation();
        node.setAttribute(ATTRIBUTE, addrtext);
    }

}
