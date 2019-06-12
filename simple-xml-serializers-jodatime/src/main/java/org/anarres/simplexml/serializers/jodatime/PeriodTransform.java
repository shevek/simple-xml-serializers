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
import org.joda.time.Period;
import org.joda.time.ReadablePeriod;
import org.simpleframework.xml.transform.Transform;

/**
 *
 * @author shevek
 */
@AutoService(TransformFactory.class)
public class PeriodTransform implements Transform<Period>, TransformFactory {

    @Override
    public Iterable<? extends Map.Entry<? extends Class<?>, ? extends Transform<?>>> newTransforms() {
        return Collections.singletonList(Maps.immutableEntry(Period.class, this));
    }

    @Nonnull
    /* pp */ static Period parse(@Nonnull String value) {
        return Period.parse(value);
    }

    @Nonnull
    /* pp */ static String format(@Nonnull ReadablePeriod value) {
        return value.toPeriod().toString();
    }

    @Override
    public Period read(String value) throws Exception {
        return parse(value);
    }

    @Override
    public String write(Period value) throws Exception {
        return format(value);
    }
}
