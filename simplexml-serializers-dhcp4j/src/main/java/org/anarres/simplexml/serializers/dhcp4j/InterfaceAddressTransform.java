package org.anarres.simplexml.serializers.dhcp4j;

import com.google.auto.service.AutoService;
import com.google.common.collect.Maps;
import java.util.Collections;
import java.util.Map;
import org.anarres.dhcp.common.address.InterfaceAddress;
import org.anarres.simplexml.serializers.common.TransformFactory;
import org.simpleframework.xml.transform.Transform;

/**
 *
 * @author shevek
 */
@AutoService(TransformFactory.class)
public class InterfaceAddressTransform implements Transform<InterfaceAddress>, TransformFactory {

    @Override
    public Iterable<? extends Map.Entry<? extends Class<?>, ? extends Transform<?>>> newTransforms() {
        return Collections.singletonList(Maps.immutableEntry(InterfaceAddress.class, this));
    }

    @Override
    public InterfaceAddress read(String value) throws Exception {
        return InterfaceAddress.forString(value);
    }

    @Override
    public String write(InterfaceAddress value) throws Exception {
        return value.toString();
    }

}
