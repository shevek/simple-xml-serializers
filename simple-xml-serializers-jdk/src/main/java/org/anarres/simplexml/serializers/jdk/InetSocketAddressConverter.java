/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.simplexml.serializers.jdk;

import com.google.auto.service.AutoService;
import com.google.common.collect.Maps;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Collections;
import java.util.Map;
import org.anarres.simplexml.serializers.common.ConverterFactory;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.convert.Converter;
import org.simpleframework.xml.stream.InputNode;
import org.simpleframework.xml.stream.OutputNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author shevek
 */
@AutoService(ConverterFactory.class)
public class InetSocketAddressConverter extends AbstractConverter implements Converter<InetSocketAddress>, ConverterFactory {

    private static final Logger LOG = LoggerFactory.getLogger(InetSocketAddressConverter.class);

    public static final String ADDRESS = "address";
    public static final String PORT = "port";

    @Override
    public Iterable<? extends Map.Entry<? extends Class<?>, ? extends Converter<?>>> newConverters(Serializer serializer) {
        return Collections.singleton(Maps.immutableEntry(InetSocketAddress.class, this));
    }

    @Override
    public InetSocketAddress read(InputNode node) throws Exception {
        String addrtext = getAttribute(node, ADDRESS);
        String porttext = getAttribute(node, PORT);
        InetAddress addr = InetAddress.getByName(addrtext);
        int port = Integer.parseInt(porttext);
        return new InetSocketAddress(addr, port);
    }

    @Override
    public void write(OutputNode node, InetSocketAddress value) throws Exception {
        InetAddress addr = value.getAddress();
        String addrtext = (addr == null) ? value.getHostName() : addr.getHostAddress();
        node.setAttribute(ADDRESS, addrtext);
        node.setAttribute(PORT, String.valueOf(value.getPort()));
    }
}
