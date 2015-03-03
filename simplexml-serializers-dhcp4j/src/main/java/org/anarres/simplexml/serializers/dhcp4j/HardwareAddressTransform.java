package org.anarres.simplexml.serializers.dhcp4j;

import com.google.auto.service.AutoService;
import com.google.common.collect.Maps;
import java.util.Collections;
import java.util.Map;
import org.anarres.simplexml.serializers.common.TransformFactory;
import org.apache.directory.server.dhcp.messages.HardwareAddress;
import org.simpleframework.xml.transform.Transform;

/**
 *
 * @author shevek
 */
@AutoService(TransformFactory.class)
public class HardwareAddressTransform implements Transform<HardwareAddress>, TransformFactory {

    @Override
    public Iterable<? extends Map.Entry<? extends Class<?>, ? extends Transform<?>>> newTransforms() {
        return Collections.singletonList(Maps.immutableEntry(HardwareAddress.class, this));
    }

    @Override
    public HardwareAddress read(String value) throws Exception {
        return HardwareAddress.fromString(value);
    }

    @Override
    public String write(HardwareAddress value) throws Exception {
        return value.getNativeRepresentation();
    }

}
