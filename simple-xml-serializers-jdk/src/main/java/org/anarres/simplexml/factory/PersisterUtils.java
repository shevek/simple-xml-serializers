/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.simplexml.factory;

import com.google.common.base.CharMatcher;
import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.base.Throwables;
import com.google.common.primitives.UnsignedBytes;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.simpleframework.xml.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author shevek
 */
public class PersisterUtils {

    // private static final Logger LOG = LoggerFactory.getLogger(PersisterUtils.class);
    private PersisterUtils() {
    }

    @Nonnull
    public static String toString(@Nonnull byte[] data, @Nonnegative int start, @Nonnegative int len) {
        StringBuilder buf = new StringBuilder();
        buf.append("Array(start=").append(start).append(", len=").append(len).append(", data=[");
        int count = Math.min(32, len);
        for (int i = 0; i < count; i++) {
            if (i > 0)
                buf.append(", ");
            int idx = start + i;
            if (idx >= data.length) {
                buf.append("OOBE!");
                break;
            }
            buf.append(UnsignedBytes.toString(data[i], 16));
        }
        if (len > 32)
            buf.append(", ...");
        buf.append("])");
        return buf.toString();
    }

    private static final Splitter SPLITTER = Splitter.on(CharMatcher.is(',')).omitEmptyStrings().trimResults();

    @Nonnull
    public static List<? extends String> split(@Nonnull String in) {
        List<String> out = SPLITTER.splitToList(in);
        // LOG.info(in + " -> " + out);
        return out;
    }

    private static boolean isPrimitiveArray(@Nonnull Class<?> type) {
        int depth = 0;
        while (type.isArray()) {
            type = type.getComponentType();
            depth++;
        }
        return type.isPrimitive() && depth > 0;
    }

    @Nullable
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static Object wrap(@Nullable Object value) {
        if (value == null)
            return value;
        if (value instanceof Type)
            return new TypeHolder((Type) value);
        if (value instanceof List<?>)
            return new ListHolder((List) value);
        if (value instanceof Map<?, ?>)
            return new MapHolder((Map) value);
        if (value instanceof byte[])
            return new ByteArrayHolder((byte[]) value);
        return value;
    }

    @Nonnull
    public static Class<?> wrapper(@Nonnull Class<?> type) {
        if (Type.class.isAssignableFrom(type))
            return TypeHolder.class;
        if (List.class.isAssignableFrom(type))
            return ListHolder.class;
        if (Map.class.isAssignableFrom(type))
            return MapHolder.class;
        if (byte[].class.isAssignableFrom(type))
            return ByteArrayHolder.class;
        return type;
    }

    @Nullable
    public static Object unwrap(@Nullable Object value) {
        if (value instanceof Holder<?>)
            return ((Holder<?>) value).getValue();
        return value;
    }

    public static void save(
            @Nonnull Serializer serializer,
            @Nonnull OutputStream out,
            @Nonnull Object value, boolean compress) throws IOException {
        try {
            if (compress)
                out = new GZIPOutputStream(out);                       // Output (compressor)

            Object serializedValue = wrap(value);
            serializer.write(serializedValue, out);
            out.close();
        } catch (Exception e) {
            Throwables.propagateIfPossible(e, IOException.class);
            throw new IOException(e);
        }
    }

    @Nonnull
    public static byte[] save(
            @Nonnull Serializer serializer,
            @Nonnull Object value, boolean compress) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();  // Output (compressed)
        save(serializer, bos, value, compress);
        return bos.toByteArray();
    }

    @Nonnull
    public static byte[] save(
            @Nonnull Serializer serializer,
            @Nonnull Object value) throws IOException {
        return save(serializer, value, true);
    }

    @Nullable
    @SuppressWarnings("unchecked")
    public static <T> T load(
            @Nonnull Serializer serializer,
            @Nonnull InputStream in,
            @Nonnull Class<T> type, boolean compress) throws IOException {
        Preconditions.checkNotNull(in, "InputStream was null.");
        Preconditions.checkNotNull(type, "Type was null.");
        Class<?> serializedType = wrapper(type);
        try {
            if (compress)
                in = new GZIPInputStream(in);
            Object serializedValue = serializer.read(serializedType, in, false);
            return type.cast(unwrap(serializedValue));
        } catch (Exception e) {
            Throwables.propagateIfPossible(e, IOException.class);
            throw new IOException(e);
        }
    }

    @Nullable
    @SuppressWarnings("unchecked")
    public static <T> T load(
            @Nonnull Serializer serializer,
            @Nonnull byte[] data,
            @Nonnull Class<T> type, boolean compress) throws IOException {
        Preconditions.checkNotNull(data, "Data was null.");
        return load(serializer, new ByteArrayInputStream(data), type, compress);
    }

    public static <T> T load(
            @Nonnull Serializer serializer,
            @Nonnull byte[] data,
            @Nonnull Class<T> type) throws IOException {
        return load(serializer, data, type, true);
    }

    @Nonnull
    public static <T> T clone(@Nonnull Serializer serializer, @Nonnull T value) throws IOException {
        @SuppressWarnings("unchecked")
        Class<T> type = (Class<T>) value.getClass();
        byte[] data = save(serializer, value, false);
        return type.cast(load(serializer, data, type, false));
    }

    @Nonnull
    public static <T> List<T> toList(@Nonnull List<T> in) {
        Preconditions.checkNotNull(in, "List was null.");
        if (in instanceof ArrayList)
            return in;
        return new ArrayList<T>(in);
    }

    @Nonnull
    public static <K, V> Map<K, V> toMap(@Nonnull Map<K, V> in) {
        Preconditions.checkNotNull(in, "Map was null.");
        if (in instanceof HashMap)
            return in;
        return new HashMap<K, V>(in);
    }
}
