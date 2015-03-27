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
import java.util.Map.Entry;
import org.anarres.simplexml.serializers.common.TransformFactory;
import org.simpleframework.xml.transform.Transform;

/**
 *
 * @author shevek
 */
@AutoService(TransformFactory.class)
public class InetAddressTransform implements Transform<InetAddress>, TransformFactory {

    @Override
    @SuppressWarnings("unchecked")
    public Iterable<? extends Entry<? extends Class<?>, ? extends Transform<?>>> newTransforms() {
        return Arrays.asList(
                Maps.immutableEntry(InetAddress.class, this),
                Maps.immutableEntry(Inet4Address.class, this),
                Maps.immutableEntry(Inet6Address.class, this));
    }

    @Override
    public InetAddress read(String value) throws Exception {
        return InetAddress.getByName(value);
    }

    @Override
    public String write(InetAddress value) throws Exception {
        return value.getHostAddress();
    }
}
