/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.simplexml.serializers.jdk;

import com.google.auto.service.AutoService;
import com.google.common.collect.Maps;
import com.google.common.io.BaseEncoding;
import java.io.IOException;
import java.util.Collections;
import java.util.Map.Entry;
import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import org.anarres.simplexml.serializers.common.TransformFactory;
import org.simpleframework.xml.transform.Transform;

/**
 *
 * @author shevek
 */
// @TODO("Write PrimitiveArrayTransform in format 1, 2, 3, 4 for all other simple primitive types. FlowScatterImpl, FlowComparatorImpl etc use it.")
@AutoService(TransformFactory.class)
public class ByteArrayTransform implements Transform<byte[]>, TransformFactory {

    @Override
    public Iterable<? extends Entry<? extends Class<?>, ? extends Transform<?>>> newTransforms() {
        return Collections.singleton(Maps.immutableEntry(byte[].class, this));
    }

    // I think this is @Nonnull
    @CheckForNull
    public static byte[] toObject(@Nonnull String value) throws IOException, ClassNotFoundException {
        return BaseEncoding.base64().decode(value);
    }

    @Nonnull
    public static String toString(@Nonnull byte[] value) throws IOException {
        return BaseEncoding.base64().encode(value);
    }

    @Override
    public byte[] read(String value) throws Exception {
        return toObject(value);
    }

    @Override
    public String write(byte[] value) throws Exception {
        return toString(value);
    }
}
