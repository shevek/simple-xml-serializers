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
import javax.annotation.Nonnull;
import org.anarres.simplexml.serializers.common.TransformFactory;
import org.joda.time.Instant;
import org.simpleframework.xml.transform.Transform;

/**
 *
 * @author shevek
 */
@AutoService(TransformFactory.class)
public class InstantTransform implements Transform<Instant>, TransformFactory {

    @Override
    public Iterable<? extends Map.Entry<? extends Class<?>, ? extends Transform<?>>> newTransforms() {
        return Collections.singletonList(Maps.immutableEntry(Instant.class, this));
    }

    @Nonnull
    /* pp */ static Instant parse(@Nonnull String value) {
        try {
            return new Instant(Long.parseLong(value));
        } catch (NumberFormatException e) {
            return Instant.parse(value);
        }
    }

    @Override
    public Instant read(String value) throws Exception {
        return parse(value);
    }

    @Override
    public String write(Instant value) throws Exception {
        return value.toString();
    }
}
