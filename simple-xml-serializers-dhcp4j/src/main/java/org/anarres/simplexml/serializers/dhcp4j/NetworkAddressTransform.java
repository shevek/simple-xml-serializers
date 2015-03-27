package org.anarres.simplexml.serializers.dhcp4j;

import com.google.auto.service.AutoService;
import com.google.common.collect.Maps;
import java.util.Collections;
import java.util.Map;
import org.anarres.dhcp.common.address.NetworkAddress;
import org.anarres.simplexml.serializers.common.TransformFactory;
import org.simpleframework.xml.transform.Transform;

/**
 *
 * @author shevek
 */
@AutoService(TransformFactory.class)
public class NetworkAddressTransform implements Transform<NetworkAddress>, TransformFactory {

    @Override
    public Iterable<? extends Map.Entry<? extends Class<?>, ? extends Transform<?>>> newTransforms() {
        return Collections.singletonList(Maps.immutableEntry(NetworkAddress.class, this));
    }

    @Override
    public NetworkAddress read(String value) throws Exception {
        return NetworkAddress.forString(value);
    }

    @Override
    public String write(NetworkAddress value) throws Exception {
        return value.toString();
    }

}
