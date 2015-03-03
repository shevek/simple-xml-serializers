/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.simplexml.serializers.jdk;

import com.google.auto.service.AutoService;
import com.google.common.collect.Maps;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Collections;
import java.util.Map.Entry;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import org.anarres.simplexml.serializers.common.TransformFactory;
import org.simpleframework.xml.transform.Transform;

/**
 *
 * @author shevek
 */
@AutoService(TransformFactory.class)
public class SerializableTransform implements Transform<Serializable>, TransformFactory {

    private static final boolean COMPRESS = false;

    public static class Inner {

        private static final SerializableTransform INSTANCE = new SerializableTransform();
    }

    @Nonnull
    public static SerializableTransform getInstance() {
        return Inner.INSTANCE;
    }

    @Override
    public Iterable<? extends Entry<? extends Class<?>, ? extends Transform<?>>> newTransforms() {
        return Collections.singleton(Maps.immutableEntry(Serializable.class, this));
    }

    @CheckForNull
    public static Serializable toObject(@Nonnull String value) throws IOException, ClassNotFoundException {
        byte[] data = ByteArrayTransform.toObject(value);
        InputStream is = new ByteArrayInputStream(data);
        if (COMPRESS)
            is = new GZIPInputStream(is);
        ObjectInputStream ois = new ObjectInputStream(is);
        return (Serializable) ois.readObject();
    }

    @Nonnull
    public static String toString(@CheckForNull Serializable value) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        OutputStream os = bos;
        if (COMPRESS)
            os = new GZIPOutputStream(os);
        ObjectOutputStream oos = new ObjectOutputStream(os);
        oos.writeObject(value);
        oos.close();
        return ByteArrayTransform.toString(bos.toByteArray());
    }

    @Override
    public Serializable read(String value) throws Exception {
        return toObject(value);
    }

    @Override
    public String write(Serializable value) throws Exception {
        return toString(value);
    }
}
