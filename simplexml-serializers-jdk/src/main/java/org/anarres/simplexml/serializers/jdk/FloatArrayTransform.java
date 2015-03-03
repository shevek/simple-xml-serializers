// AUTOGENERATED from ShortArray{Converter,Transform}.java
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.simplexml.serializers.jdk;

import com.google.auto.service.AutoService;
import com.google.common.base.Joiner;
import com.google.common.collect.Maps;
import com.google.common.primitives.Floats;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;
import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import org.anarres.simplexml.factory.PersisterUtils;
import org.anarres.simplexml.serializers.common.TransformFactory;
import org.simpleframework.xml.transform.Transform;

/**
 *
 * @author shevek
 */
@AutoService(TransformFactory.class)
public class FloatArrayTransform implements Transform<float[]>, TransformFactory {

    @Override
    public Iterable<? extends Entry<? extends Class<?>, ? extends Transform<?>>> newTransforms() {
        return Collections.singleton(Maps.immutableEntry(float[].class, this));
    }

    @CheckForNull
    public static float[] toObject(@Nonnull String value) throws IOException, ClassNotFoundException {
        List<? extends String> values = PersisterUtils.split(value);
        float[] out = new float[values.size()];
        for (int i = 0; i < out.length; i++)
            out[i] = Float.parseFloat(values.get(i));
        return out;
    }

    @Nonnull
    public static String toString(@Nonnull float[] value) throws IOException {
        return Joiner.on(",").join(Floats.asList(value));
    }

    @Override
    public float[] read(String value) throws Exception {
        return toObject(value);
    }

    @Override
    public String write(float[] value) throws Exception {
        return toString(value);
    }
}