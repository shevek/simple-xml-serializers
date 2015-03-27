/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.simplexml.serializers.jdk;

import com.google.auto.service.AutoService;
import com.google.common.collect.Maps;
import java.util.Collections;
import java.util.Map.Entry;
import java.util.UUID;
import org.anarres.simplexml.serializers.common.TransformFactory;
import org.simpleframework.xml.transform.Transform;

/**
 *
 * @author shevek
 */
@AutoService(TransformFactory.class)
public class UUIDTransform implements Transform<UUID>, TransformFactory {

    @Override
    @SuppressWarnings("unchecked")  // Generic array creation
    public Iterable<? extends Entry<? extends Class<?>, ? extends Transform<?>>> newTransforms() {
        return Collections.singletonList(Maps.immutableEntry(UUID.class, this));
    }

    @Override
    public UUID read(String value) throws Exception {
        return UUID.fromString(value);
    }

    @Override
    public String write(UUID value) throws Exception {
        return value.toString();
    }
}
