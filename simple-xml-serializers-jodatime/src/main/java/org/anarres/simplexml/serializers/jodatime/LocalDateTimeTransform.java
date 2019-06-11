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
import org.joda.time.LocalDateTime;
import org.simpleframework.xml.transform.Transform;

/**
 *
 * @author shevek
 */
@AutoService(TransformFactory.class)
public class LocalDateTimeTransform implements Transform<LocalDateTime>, TransformFactory {

    @Override
    public Iterable<? extends Map.Entry<? extends Class<?>, ? extends Transform<?>>> newTransforms() {
        return Collections.singletonList(Maps.immutableEntry(LocalDateTime.class, this));
    }

    @Override
    public LocalDateTime read(String value) throws Exception {
        return LocalDateTime.parse(value);
    }

    @Override
    public String write(LocalDateTime value) throws Exception {
        return value.toString();
    }
}
