/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.simplexml.serializers.jodatime;

import com.google.auto.service.AutoService;
import com.google.common.collect.Maps;
import java.util.Collections;
import java.util.Map;
import org.anarres.simplexml.serializers.common.TransformFactory;
import org.joda.time.ReadableInstant;
import org.simpleframework.xml.transform.Transform;

/**
 *
 * @author shevek
 */
@AutoService(TransformFactory.class)
public class ReadableInstantTransform implements Transform<ReadableInstant>, TransformFactory {

    @Override
    public Iterable<? extends Map.Entry<? extends Class<?>, ? extends Transform<?>>> newTransforms() {
        return Collections.singletonList(Maps.immutableEntry(ReadableInstant.class, this));
    }

    @Override
    public ReadableInstant read(String value) throws Exception {
        return InstantTransform.parse(value);
    }

    @Override
    public String write(ReadableInstant value) throws Exception {
        return InstantTransform.format(value);
    }
}
