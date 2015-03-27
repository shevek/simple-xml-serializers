/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.simplexml.serializers.jdk;

import com.google.auto.service.AutoService;
import com.google.common.collect.Maps;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.util.Arrays;
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
public class InetAddressConverter extends AbstractConverter implements Converter<InetAddress>, ConverterFactory {

    public static final String ATTRIBUTE = "address";

    @Override
    public Iterable<? extends Map.Entry<? extends Class<?>, ? extends Converter<?>>> newConverters(Serializer serializer) {
        return Arrays.asList(
                Maps.immutableEntry(InetAddress.class, this),
                Maps.immutableEntry(Inet4Address.class, this),
                Maps.immutableEntry(Inet6Address.class, this));
    }

    @Override
    public InetAddress read(InputNode node) throws Exception {
        String addrtext = getAttribute(node, ATTRIBUTE);
        return InetAddress.getByName(addrtext);
    }

    @Override
    public void write(OutputNode node, InetAddress value) throws Exception {
        String addrtext = value.getHostAddress();
        node.setAttribute(ATTRIBUTE, addrtext);
    }
}
